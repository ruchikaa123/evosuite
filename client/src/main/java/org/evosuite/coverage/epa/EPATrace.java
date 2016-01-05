package org.evosuite.coverage.epa;

import org.evosuite.testcase.execution.ExecutionTrace;
import org.evosuite.testcase.execution.MethodCall;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EPATrace {
	private List<EPATransition> epaTransitions;

	public EPATrace(ExecutionTrace executionTrace, EPA epa) {
		epaTransitions = new ArrayList<>();
		EPAState originState = epa.getInitialState();

		for (MethodCall methodCallExecution : executionTrace.getMethodCalls()) {
			String actionName = getCleanMethodName(methodCallExecution.methodName);
			actionName = actionName.equals("<init>") ? epa.getName() : actionName;
			if (!epa.isActionInEPA(actionName))
				continue;
			
			//final EPAState destinationState = epa.getStateByName(methodCallexecution.endState);
			// TODO: Remove this once MethodCall.endState is implemented
			final EPAState destinationState = epa.temp_anyPossibleDestinationState(originState, actionName);
			if (destinationState == null)
				return;

			epaTransitions.add(new EPATransition(originState, actionName, destinationState));
			originState = destinationState;
		}
	}
	private String getCleanMethodName(final String methodName) {
		String[] splitted = methodName.split("\\(");
		return splitted[0];
	}

	public List<EPATransition> getEpaTransitions() {
		return epaTransitions;
	}
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("EPATrace [");
		for (int i=0; i<this.epaTransitions.size();i++) {
			EPATransition epaTransition = epaTransitions.get(i);
			String actionName = epaTransition.getActionName();
			if (i>0) {
				buff.append(",");
			}
			buff.append(actionName);
		}
		buff.append("]");
		return buff.toString();
	}
}