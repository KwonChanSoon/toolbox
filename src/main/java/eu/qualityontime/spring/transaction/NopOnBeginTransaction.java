package eu.qualityontime.spring.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

@Component
class NopOnBeginTransaction implements IOnBeginTransaction {
  @Override
  public void execute(ProceedingJoinPoint joinPoint, TransactionStatus transactionStatus) {
    //Sample empty operation
  }

}
