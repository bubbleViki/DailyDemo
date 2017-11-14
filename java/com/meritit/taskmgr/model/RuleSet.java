package com.meritit.taskmgr.model;

import java.io.Serializable;
import java.util.List;

public class RuleSet implements Serializable, Comparable<RuleSet>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1608320078739497712L;
	/**
	 * 规则ID
	 */
	private String ruleid;
	/**
	 * 规则集合
	 */
	private List<Rule> ruleList;
	
	public String getRuleid() {
		return ruleid;
	}
	public void setRuleid(String ruleid) {
		this.ruleid = ruleid;
	}
	public List<Rule> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<Rule> ruleList) {
		this.ruleList = ruleList;
	}
	@Override
	public int compareTo(RuleSet ruleSet) {
		int compare = this.getRuleid().compareTo(ruleSet.getRuleid());
		if(compare > 0){
			return 1;
		}else if(compare < 0){
			return -1;
		}else{
			return 0;
		}
	}
}
