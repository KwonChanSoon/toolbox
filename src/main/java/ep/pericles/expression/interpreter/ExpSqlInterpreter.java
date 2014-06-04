package ep.pericles.expression.interpreter;

import static ep.pericles.AppCollections.Map;
import static ep.pericles.AppObjects.cast;
import static ep.pericles.AppStrings.rs;
import static ep.pericles.expression.BinaryOp.*;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.*;
import com.google.common.collect.*;

import ep.pericles.*;
import ep.pericles.collections.*;
import ep.pericles.expression.*;
import ep.pericles.tuple.Pair;
import ep.pericles.visitor.*;

public class ExpSqlInterpreter extends ReflectiveFunctionVisitor<String> implements ExpInterpreter {
  /**
   * optional parameter prefix not to mix whese paras with any other "manaul" parameter;
   */
  private String paramPrefix = "xyz_";
  private String print;
  private Map<String, Object> params = Map();
  private Function<Object, String> defaultField = Functions.toStringFunction();
  private Map<Object, String> fieldMapping = DefaultValueMap.decorate(new HashMap<Object, String>(), defaultField);
  private static Map<BinaryOp, String> binaryOp = new ImmutableMap.Builder<BinaryOp, String>()
      .put(EQ, " = ").put(NEQ, " <> ").put(GT, " > ").put(LT, " < ").put(GTE, " >= ")
      .put(LTE, " <= ").put(LIKE, " LIKE ").build();
  private static Map<CompositeOp, String> compositeOp = new ImmutableMap.Builder<CompositeOp, String>()
      .put(CompositeOp.AND, " AND ").put(CompositeOp.OR, " OR ").build();
  private FunctionVisitor<Object> valueMappings = new ValueTypeMapping();
  private IVariableContext context = new NullVariableContext();
  private Map<String, String> functionMapping = ImmutableMap.of();

  public ExpSqlInterpreter() {
  }

  public ExpSqlInterpreter(String paramPrefix) {
    this.paramPrefix = paramPrefix;
  }

  public ExpSqlInterpreter(String paramPrefix, Map<Object, String> fieldMapping) {
    this.paramPrefix = paramPrefix;
    setFieldMapping(fieldMapping);
  }

  public ExpSqlInterpreter(String paramPrefix, Map<Object, String> fieldMapping, FunctionVisitor<Object> valueMappings) {
    this.paramPrefix = paramPrefix;
    setFieldMapping(fieldMapping);
    setValueMappings(valueMappings);
  }

  public ExpSqlInterpreter(String paramPrefix, FunctionVisitor<Object> valueMappings) {
    this.paramPrefix = paramPrefix;
    setValueMappings(valueMappings);
  }

  @Override
  public void interpret(Exp exp) {
    print = visit(exp);
  }

  @Override
  public String visit(Object o) {
    String comment = "";
    if (o instanceof Exp) {
      Exp e = (Exp) o;
      if (!StringUtils.isBlank(e.getExpId())) {
        comment = "/* " + e.getExpId() + " */";
      }
    }
    return comment + super.visit(o);
  }

  public Pair<String, Map<String, Object>> getQuery() {
    return Pair.of(getSql(), getParams());
  }

  public String getSql() {
    return print.toString();
  }

  public Map<String, Object> getParams() {
    return ImmutableMap.copyOf(params);
  }

  private int counter = 1;

  protected String paramName() {
    return paramPrefix + (counter++);
  }

  protected String paramPlaceholder(String pname) {
    return ":" + pname;
  }

  @SuppressWarnings("unchecked")
  public void setFieldMapping(Map<? extends Object, String> fieldMapping) {
    this.fieldMapping = DefaultValueMap.decorate((Map<Object, String>) fieldMapping, Functions.toStringFunction());
  }

  /**
   * Visitor function
   */
  public String visitFieldExp(FieldExp e) {
    return fieldMapping.get(e.getName());
  }

  /**
   * Visitor function
   */
  public String visitSimpleConditionExp(SimpleConditionExp e) {
    String paramName = paramName();
    putParamValue(paramName, e.getValue());
    return (fieldMapping.get(e.getField().getName())) + (binaryOp.get(e.getOp())) + (paramPlaceholder(paramName));
  }

  private void putParamValue(String paramName, ValueExp e) {
    params.put(paramName, valueMappings.visit(valueOf(e)));
  }

  private Object valueOf(ValueExp e) {
    if (e instanceof LiteralExp) {
      return ((LiteralExp) e).getValue();
    }
    else if (e instanceof VariableExp) {
      return context.get(((VariableExp) e).getVariableName());
    }
    throw new Defect("Not handled!");
  }

  /**
   * Visitor function
   */
  public String visitNotExp(NotExp e) {
    return "NOT (" + visit(e.getExp()) + ")";
  }

  /**
   * Visitor function
   */
  public String visitCompositeExp(CompositeExp e) {
    String op = visit(e.getOp());
    Iterable<String> subexps = Iterables.transform(e.getExps(), new FunctionVisitorFunction<String>(this));
    return Joiner.on(op).join(subexps);
  }

  /**
   * Visitor function
   */
  public String visitCompositeOp(CompositeOp op) {
    return compositeOp.get(op);
  }

  /**
   * Visitor function
   */
  public String visitPrecedenceExp(PrecedenceExp e) {
    return "(" + visit(e.getExp()) + ")";
  }

  public FunctionVisitor<Object> getValueMappings() {
    return valueMappings;
  }

  public void setValueMappings(FunctionVisitor<Object> valueMappings) {
    this.valueMappings = valueMappings;
  }

  /**
   * Visitor function
   */
  public String visitFunctionExp(FunctionExp e) {
    if (!functionMapping.containsKey(e.getName())) {
      throw new Defect(rs("No mapping for function '{}'", e.getName()));
    }
    String baseSql = functionMapping.get(e.getName());
    if (AppCollections.isEmpty(e.getParams())) {
      return baseSql;
    }
    Map<String, Object> sqlParams = Map();
    FunctionNameGenerator fnPGen = functionNameParamNameGenerator(e.getName());
    for (int i = 0; i < e.getParams().size(); i++) {
      String fn_param_name = fnPGen.get();
      String placeholderName = paramPlaceholder(fn_param_name);
      sqlParams.put("" + i, placeholderName);
      putParamValue(fn_param_name, e.getParams().get(i));
    }
    return AppStrings.replace(baseSql, sqlParams);
  }

  private Map<String, Integer> nrOfFunctionCall = cast(LazyMap.decorate(new HashMap<Object, Integer>(), Functions.constant(0)));

  private FunctionNameGenerator functionNameParamNameGenerator(String name) {
    Integer currentCount = nrOfFunctionCall.get(name);
    currentCount += 1;
    nrOfFunctionCall.put(name, currentCount);
    return new FunctionNameGenerator(paramPrefix, name, currentCount);
  }

  public ExpSqlInterpreter setFunctionMapping(Map<String, String> functionMapping) {
    this.functionMapping = functionMapping;
    return this;
  }

  public ExpSqlInterpreter setContext(IVariableContext context) {
    this.context = context;
    return this;
  }
}
