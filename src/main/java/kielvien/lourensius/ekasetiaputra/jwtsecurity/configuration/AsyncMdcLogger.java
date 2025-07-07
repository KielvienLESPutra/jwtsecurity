package kielvien.lourensius.ekasetiaputra.jwtsecurity.configuration;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.task.AsyncTaskExecutor;

@Configurable
public class AsyncMdcLogger implements AsyncTaskExecutor {

	private AsyncTaskExecutor executorTask;

	public AsyncMdcLogger(Executor executor) {
		executorTask = (AsyncTaskExecutor) executor;
	}

	@Override
	public void execute(Runnable task) {
		executorTask.execute(wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return AsyncTaskExecutor.super.submit(wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return AsyncTaskExecutor.super.submit(wrap(task, MDC.getCopyOfContextMap()));
	}

	private Runnable wrap(Runnable task, Map<String, String> context) {
		return () -> {
			Map<String, String> lastTracingId = MDC.getCopyOfContextMap();
			if (lastTracingId != null) {
				MDC.setContextMap(context);
			} else {
				MDC.clear();
			}

			try {
				task.run();
			} finally {
				if (lastTracingId != null) {
					MDC.setContextMap(lastTracingId);
				} else {
					MDC.clear();
				}
			}
		};
	}

	private <T> Callable<T> wrap(Callable<T> task, Map<String, String> context) {
		return () -> {
			Map<String, String> lastTracingId = MDC.getCopyOfContextMap();
			if (lastTracingId != null) {
				MDC.setContextMap(context);
			} else {
				MDC.clear();
			}

			try {
				return task.call();
			} finally {
				if (lastTracingId != null) {
					MDC.setContextMap(lastTracingId);
				} else {
					MDC.clear();
				}
			}
		};
	}
}
