package ep.pericles.spring.transaction;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

@Component
@Aspect
public class BeginTransactionHook {
  private static Logger log = LoggerFactory.getLogger(BeginTransactionHook.class);
  @Autowired
  private List<IOnBeginTransaction> eventhandlers;

  @Around("execution(* org.springframework.transaction.PlatformTransactionManager.getTransaction(..))")
  Object logAroundTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
    log.trace("logAroundTransaction() is running!");
    TransactionStatus ret = (TransactionStatus) joinPoint.proceed();
    for (IOnBeginTransaction eh : eventhandlers) {
      eh.execute(joinPoint, ret);
    }
    return ret;
  }

  public BeginTransactionHook() {

  }
}
