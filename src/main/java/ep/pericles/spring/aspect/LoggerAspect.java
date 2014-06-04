package ep.pericles.spring.aspect;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.*;
import org.springframework.stereotype.Component;

import com.google.common.base.Stopwatch;

@Component("LoggerAspect")
public class LoggerAspect {

  private Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

  public Object logExecutionTime(ProceedingJoinPoint jointPoint) throws Throwable {
    Stopwatch watch = Stopwatch.createUnstarted().start();
    Object ret = jointPoint.proceed();
    watch.stop();
    Long ms = watch.elapsed(TimeUnit.MILLISECONDS);
    StringBuilder sb = new StringBuilder(jointPoint.getTarget().getClass().getName());
    sb.append(".");
    sb.append(jointPoint.getSignature().getName());
    //    sb.append("(");
    //    for(Object arg : jointPoint.getArgs()) {
    //      sb.append(arg).append(",");
    //    }
    //    if(jointPoint.getArgs().length > 0) {
    //      sb.deleteCharAt(sb.length() - 1);
    //    }
    //    sb.append(")");
    logger.info("\t{}\tms elapsed for\t{}", ms, sb.toString());
    return ret;
  }

}
