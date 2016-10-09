package com.ddz.msg;

import java.util.List;

import com.ddz.entity.Poker;

public class CurrentPokerMsg extends BaseMsg {
	/**
	 * 上两家所出的牌
	 */
	private List<Poker>[] currentpoker;
	/**
	 * 房间号,作为报文发送目的地的标志
	 */
	private int rid;

	public CurrentPokerMsg(List<Poker>[] currentpoker) {
		this.currentpoker = currentpoker;
	}

	@Override
	public void doBiz() {
		// TODO Auto-generated method stub
		
	}

}
