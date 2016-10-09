package com.ddz.biz;

import java.awt.Container;
import java.awt.Point;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;

import com.ddz.entity.Player;
import com.ddz.entity.Poker;
import com.ddz.entity.PokerType;
import com.ddz.entity.Poker_Index;
import com.ddz.entity.Room;
import com.ddz.gui.PlayFrame;
import com.ddz.gui.PlayFrame.PokeCard;

public class Common {

	public static void move(Poker poker, Point from, Point to) {
	}

	public static boolean roomIsAvailable(Room room, int pos) {
		if (room.getStatus() != Room.PLAYING) {
			if (pos == 0) {
				if (room.getLeftPlayer().length() > 0)
					return false;
				return true;
			}
			if (pos == 1) {
				if (room.getRightPlayer().length() > 0)
					return false;
				return true;
			}
			if (pos == 2) {
				if (room.getTopPlayer().length() > 0)
					return false;
				return true;
			}
		}
		return false;
	}

	public static void enterRoom(Room room, int pos, Player player) {
		if (pos == 0) {
			room.setLeftPlayer(player.getUser().getUsername());
			player.setStatus(Player.INROOM);
			return;
		}
		if (pos == 1) {
			room.setRightPlayer(player.getUser().getUsername());
			player.setStatus(Player.INROOM);
			return;
		}
		if (pos == 2) {
			room.setTopPlayer(player.getUser().getUsername());
			player.setStatus(Player.INROOM);
			return;
		}
	}

	/**
	 * 判断所出牌的类型
	 * 
	 * @param list
	 *            按点数从小到大排序后的扑克牌序列
	 * @return
	 */
	public static PokerType JugdePokerType(List<PokeCard> list) {
		int len = list.size();
		// 可能牌型：单张、对子、三张、三带一、王炸
		if (len <= 4) {
			if (list.size() > 0
					&& Common.getValue(list.get(0)) == Common.getValue(list
							.get(len - 1))) {
				switch (len) {
				case 1:
					return PokerType.p1;
				case 2:
					return PokerType.p2;
				case 3:
					return PokerType.p3;
				case 4:
					return PokerType.p4;
				}
			}
			// 王炸,化为对子返回
			if (len == 2 && list.get(1).getColorPoint() / 13 == 4)
				return PokerType.p2;
			// 当第一张和最后一张不同时,3带1
			if (4 == len
					&& ((Common.getValue(list.get(0)) == Common.getValue(list
							.get(len - 2))) || Common.getValue(list.get(1)) == Common
							.getValue(list.get(len - 1))))
				return PokerType.p31;
			else {
				return PokerType.p0;
			}
		}
		if (len >= 5) {
			// 求相同数字最大出现次数
			Poker_Index Poker_Index = new Poker_Index();
			for (int i = 0; i < 4; i++)
				Poker_Index.a[i] = new ArrayList<Integer>();
			// 求出各种数字出现频率
			Common.getMax(Poker_Index, list); // a[0,1,2,3]分别表示重复1,2,3,4次的牌
			// 3带2 -----必含重复3次的牌
			if (Poker_Index.a[2].size() == 1 && Poker_Index.a[1].size() == 1
					&& len == 5)
				return PokerType.p32;
			// 4带2(单,双)
			if (Poker_Index.a[3].size() == 1 && len == 6)
				return PokerType.p411;
			if (Poker_Index.a[3].size() == 1 && Poker_Index.a[1].size() == 2
					&& len == 8)
				return PokerType.p422;
			// 单连,保证不存在王
			if ((Common.getValue(list.get(0)) != 14)
					&& (list.get(0).getColorPoint() / 13 != 4)
					&& (Poker_Index.a[0].size() == len)
					&& (Common.getValue(list.get(0))
							- Common.getValue(list.get(len - 1)) == len - 1))
				return PokerType.p123;
			// 连对
			if (Poker_Index.a[1].size() == len / 2
					&& len % 2 == 0
					&& len / 2 >= 3
					&& (Common.getValue(list.get(0))
							- Common.getValue(list.get(len - 1)) == (len / 2 - 1)))
				return PokerType.p1122;
			// 飞机不带任何牌
			if (Poker_Index.a[2].size() == len / 3
					&& (len % 3 == 0)
					&& (Common.getValue(list.get(0))
							- Common.getValue(list.get(len - 1)) == (len / 3 - 1)))
				return PokerType.p111222;
			// 飞机带n单,n/2对
			if (Poker_Index.a[2].size() == len / 4
					&& ((Integer) (Poker_Index.a[2].get(len / 4 - 1))
							- (Integer) (Poker_Index.a[2].get(0)) == len / 4 - 1))
				return PokerType.p11122234;
			// 飞机带n双
			if (Poker_Index.a[2].size() == len / 5
					&& Poker_Index.a[1].size() == len / 5
					&& ((Integer) (Poker_Index.a[2].get(len / 5 - 1))
							- (Integer) (Poker_Index.a[2].get(0)) == len / 5 - 1))
				return PokerType.p1112223344;

		}
		return PokerType.p0;
	}

	/**
	 * 检查是否能出牌
	 * 
	 * @param c
	 *            我所出的牌
	 * @param current
	 *            当前上两家所出的牌
	 * @return
	 * 
	 *         张数不同直接过滤
	 */
	public static boolean checkCards(List<PokeCard> c, List<PokeCard> last) {
		PokerType cType = Common.JugdePokerType(c);
		PokerType lType = Common.JugdePokerType(last);

		if (lType == PokerType.p2 && last.get(0).getColorPoint() / 13 == 4)
			return false;
		if (cType == PokerType.p2 && c.get(0).getColorPoint() / 13 == 4)
			return true;
		if (cType == PokerType.p4 && lType != PokerType.p4) {
			return true;
		}

		// 如果张数不同且不为炸弹直接过滤
		if (c.size() != last.size())
			return false;
		// 比较我的出牌类型
		if (cType != lType) {
			return false;
		}
		// 比较出的牌是否要大
		// 王炸弹

		// 单牌,对子,3带,4炸弹
		if (cType == PokerType.p1 || cType == PokerType.p2
				|| cType == PokerType.p3 || cType == PokerType.p4) {
			if (Common.getValue(c.get(0)) <= Common.getValue(last.get(0))) {
				return false;
			} else {
				return true;
			}
		}
		// 顺子,连队，飞机裸
		if (cType == PokerType.p123 || cType == PokerType.p1122
				|| cType == PokerType.p111222) {
			if (Common.getValue(c.get(0)) <= Common.getValue(last.get(0)))
				return false;
			else
				return true;
		}
		// 按重复多少排序
		// 3带1,3带2 ,飞机带单，双,4带1,2,只需比较第一个就行，独一无二的
		if (cType == PokerType.p31 || cType == PokerType.p32
				|| cType == PokerType.p411 || cType == PokerType.p422
				|| cType == PokerType.p11122234
				|| cType == PokerType.p1112223344) {
			if (Common.getOrder2(c) < Common.getOrder2(last))
				return false;
		}
		return true;
	}

	/**
	 * 得到重复最多次的点数
	 * 
	 * @param list
	 *            按点数从大到小排列的序列
	 * @return
	 */
	public static void pokeOrder(List<PokeCard> list) {
		int len = list.size();
		int a[] = new int[20];
		int b[]=new int[20];
		for (int i = 0; i < 20; i++)
			a[i] = b[i]=0;
		for (int i = 0; i < len; i++) {
			a[Common.getValue(list.get(i))]++;
		}
		int max = 0;
		for (int i = 0; i < 20; i++) {
			max = 0;
			for (int j = 19; j >= 0; j--) {
				if (a[j] > a[max])
					max = j;
			}
			b[max]=i;
			a[max] = 0;
		}
		final int[] c=new int[20];
		for(int i=0;i<20;i++)
			c[i]=b[i];
		Collections.sort(list, new Comparator<PokeCard>() {
			@Override
			public int compare(PokeCard o1, PokeCard o2) {
				return c[Common.getValue(o2)]-c[Common.getValue(o1)];
			}
		});
	}

	public static int getOrder2(List<PokeCard> list) {
		List<PokeCard> list2 = new ArrayList<PokeCard>(list);
		int len = list2.size();
		int a[] = new int[20];
		for (int i = 0; i < 20; i++)
			a[i] = 0;
		for (int i = 0; i < len; i++) {
			a[Common.getValue(list2.get(i))]++;
		}
		int max = 0;
		for (int i = 0; i < 20; i++) {
			max = 0;
			for (int j = 19; j >= 0; j--) {
				if (a[j] > a[max])
					max = j;
			}
			return max;
		}
		return max;
	}

	/**
	 * 得到出现1,2,3,4次数的牌有几种
	 */
	public static void getMax(Poker_Index poker_index, List<PokeCard> list) {
		int count[] = new int[14];// 1-13各算一种,王算第14种
		for (int i = 0; i < 14; i++)
			count[i] = 0;
		for (int i = 0, len = list.size(); i < len; i++) {
			if (list.get(i).getColorPoint() / 13 == 4)
				count[13]++;
			else
				count[Common.getValue(list.get(i)) - 2]++;
		}
		for (int i = 0; i < 14; i++) {
			switch (count[i]) {
			case 1:
				poker_index.a[0].add(i + 1);
				break;
			case 2:
				poker_index.a[1].add(i + 1);
				break;
			case 3:
				poker_index.a[2].add(i + 1);
				break;
			case 4:
				poker_index.a[3].add(i + 1);
				break;
			}
		}

	}

	/**
	 * 隐藏之前出过的牌
	 * 
	 * @param list
	 */
	public static void hideCards(List<Poker> list) {
		for (int i = 0, len = list.size(); i < len; i++) {
			list.get(i).setVisible(false);
		}
	}

	/**
	 * 获得扑克牌点数
	 * 
	 * @param poker
	 * @return
	 */
	public static int getValue(PokeCard poker) {
		int i = poker.getColorPoint() % 13;
		if (i == 1)
			i += 13;
		if (i == 0)
			i += 13;
		if (poker.getColorPoint() / 13 == 4)
			i += 2;// 是王
		return i;
	}
}
