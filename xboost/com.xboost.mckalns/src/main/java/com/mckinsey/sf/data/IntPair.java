package com.mckinsey.sf.data;

import com.mckinsey.sf.constants.IConstants;

/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：May 3, 2017
* @version        
*/
public class IntPair implements IConstants{

	private int choice;
	private Score scoreIndex;
	
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
	}
	public Score getScoreIndex() {
		return scoreIndex;
	}
	public void setScoreIndex(Score scoreIndex) {
		this.scoreIndex = scoreIndex;
	}
	public IntPair(int choice, Score scoreIndex) {
		super();
		this.choice = choice;
		this.scoreIndex = scoreIndex;
	}
	
	
}
