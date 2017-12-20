package com.hivescm.search.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.hivescm.common.serialize.api.json.GsonSerialize;

@Aspect
@Component
public class SearchApiProxy {
	@Around("execution(* com.hivescm.escenter.service.ESSearchServiceImpl.*(..))")
	public Object proxySearchApi(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			return pjp.proceed();
		} finally {
			long useTime = System.currentTimeMillis() - start;
			StringBuilder builder = new StringBuilder(200);
			builder.append(pjp.getStaticPart().toShortString());
			builder.append("|");
			builder.append("args:");
			builder.append(GsonSerialize.INSTANCE.encode(pjp.getArgs()));
			builder.append("|");
			builder.append("useTime:" + useTime);
			SearchLogger.log(builder.toString());
		}
	}
}
