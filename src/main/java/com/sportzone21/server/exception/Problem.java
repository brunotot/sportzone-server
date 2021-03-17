package com.sportzone21.server.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public final class Problem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer status;
	private String title;
	private String details;
	private List<String> stacktraceLines;

	Problem(Integer status, String title, String details) {
		this.title = title;
		this.status = status;
		this.details = details;
	}

	public void setStacktraceLines(StackTraceElement[] stacktraceLines) {
		this.stacktraceLines = Arrays.stream(stacktraceLines)
				.map(line -> line.getClassName() + ":" + line.getMethodName() + "[" + line.getLineNumber() + "]")
				.collect(Collectors.toList());
	}

}