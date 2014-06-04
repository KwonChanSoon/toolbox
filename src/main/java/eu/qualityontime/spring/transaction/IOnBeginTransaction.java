package eu.qualityontime.spring.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.TransactionStatus;

public interface IOnBeginTransaction {
  public void execute(ProceedingJoinPoint joinPoint, TransactionStatus transactionStatus);
}
