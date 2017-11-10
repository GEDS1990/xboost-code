package com.mckinsey.sf.data;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class GroupCtx {
	private String scope;
	private String from;
	private String to;
	private TimeWindow fromTW;
	private TimeWindow toTW;
	private String[] skills;
	private boolean fixed;
	private int priority;

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public TimeWindow getFromTW() {
		return fromTW;
	}

	public void setFromTW(TimeWindow fromTW) {
		this.fromTW = fromTW;
	}

	public TimeWindow getToTW() {
		return toTW;
	}

	public void setToTW(TimeWindow toTW) {
		this.toTW = toTW;
	}

	public String[] getSkills() {
		return skills;
	}

	public void setSkills(String[] skills) {
		this.skills = skills;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	

	public String key() {
		StringBuffer sb = new StringBuffer();
		sb.append(scope);
		sb.append("|");
		sb.append(from);
		sb.append("|");
		sb.append(to);
		sb.append("|");
		sb.append(fromTW);
		sb.append("|");
		sb.append(toTW);
		sb.append("|");
		// TODO skills is empty
		sb.append("");
		sb.append("|");
		sb.append(fixed);
		sb.append("|");
		sb.append(priority);
		return sb.toString();
	}

}
