import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
public class SuoHaFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SuoHa suoha=new SuoHa();	
	}
}

class SuoHa extends JFrame{

	private int CARD_NUM=28;//牌的总数
	private Card[] card=new Card[CARD_NUM];//模拟CARD_NUM张牌对象
	public Card[] pcCard=new Card[4];//电脑可能的四张明牌
	public Card[] plCard=new Card[4];//玩家可能的四张明牌
	private Card pcDiPai=new Card();//电脑的底牌
	private Card plDiPai=new Card();//玩家的底牌
//	private int money;//赌注的总数
//	private int stake;//每一次下的注码（赌注）
	private int round;//发牌的轮数，小于5：用来判断中间是否有人不跟
	private int result=1;//赌局的结局:0代表电脑赢了（可能是最后摊牌赢或是中途玩家不跟了），1代表玩家赢了（可能是最后摊牌赢或是中途电脑不跟了）
	private int cardNum=0;//代表已经发了多少张牌了。最大值为10，
	private String[] CardType={"方片","梅花","红桃","黑桃"};//花色映射
	private String[] CardNum={"8","9","十","J","Q","K","A"};//面值映射
	private String[] ResultStr={"同花顺","铁支","葫芦","同花","顺子","三条","两对","一对","散牌"};
	private int rankPc=ResultStr.length-1,rankPl=rankPc;//默认等级是散牌
//	private int[] mipai=new int[6];
	private static final int DEFAULT_WIDTH=800;
	private static final int DEFAULT_HEIGHT=800;
	public SuoHa()
	{
		Player player=new Player();
		Player pc=new Player();
		/*
		setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
		setTitle("SuoHa Game");
		*/
		shuffle();//洗牌
		//发底牌
		deal(player,++cardNum);//发底牌给玩家
		plDiPai=card[cardNum];//玩家底牌的
		deal(pc,++cardNum);//发第牌 此时 发出的牌数cardNum==2，轮数为round=1
		pcDiPai=card[cardNum];//底牌的
		round=1;
		showCard(round);
		//发第二轮，即第一张明牌
		deal(player,++cardNum);//发第i+2轮牌
		plCard[round-1]=card[cardNum];//保存player的最后一张明牌
		deal(pc,++cardNum);//发第牌
		pcCard[round-1]=card[cardNum];//保存pc的最后一张明牌
		round=2;
		showCard(round);
		//第二轮发牌结束
		//在for循环里面发第三，四轮，五轮的牌
		for(int i=0;i<3;i++){
			if ( plCard[i].getNum()>pcCard[i].getNum() || (plCard[i].getNum()==pcCard[i].getNum()&& plCard[i].getType()>pcCard[i].getType()))
			{
				//player下注了，等待pc跟不跟
				if(round!=5 && (!pc.follow(plCard,round)))//第五轮是不用问跟不跟的了
				{
					result=1;
					break;
				}	
				deal(player,++cardNum);
				plCard[i+1]=card[cardNum];
				deal(pc,++cardNum);
				pcCard[i+1]=card[cardNum];
				round++;
				showCard(round);
			}
			else
			{
				//pc下注了，等待player跟不跟
				if(round!=5 && (!player.follow(pcCard,round)) )
				{
					result=0;
					break;
				}		
				deal(pc,++cardNum);
				pcCard[i+1]=card[cardNum];
				deal(player,++cardNum);
				plCard[i+1]=card[cardNum];
				round++;
				showCard(round);
			}
		}
		if(round==5)//五轮都发完整了
		{
			if(judgeResult())
				result=1;
			else
				result=0;
		}
		showCard(round);
		if(result==1)
			System.out.println("你的牌是："+ResultStr[rankPl]+"\n"+"电脑的牌是："+ResultStr[rankPc]+"\n"+"你赢了");
		else
			System.out.println("你的牌是："+ResultStr[rankPl]+"\n"+"电脑的牌是："+ResultStr[rankPc]+"\n"+"你输了");
	}

	//模拟洗牌函数
	public void shuffle()
	{
		for(int i=0;i<4;i++)
			for(int j=0;j<7;j++)
				card[i*7+j]=new Card(i,j);
		Card temp;
		java.util.Random random=new java.util.Random();
		for(int i=0;i<1000;i++)
		{
			int n1=random.nextInt(28);
			int n2=random.nextInt(28);
			temp=card[n1];
			card[n1]=card[n2];
			card[n2]=temp;
		}
	}
	//模拟发牌发牌ringring
	public void deal(Player competitor,int ring)
	{
		if(ring !=1&&ring!=2)//发第一张牌是盖着的
			card[CARD_NUM-ring].setStatus(true);//设置明牌
		competitor.receCard(card[CARD_NUM-ring]);

	}
	//判断最后输赢的函数 返回true：代表玩家赢，返回false：代表电脑赢
	public boolean judgeResult()
	{
		int i,j;
		
		Card[] tempPc=new Card[5];
		Card[] tempPl=new Card[5];
		Card maxPc,maxPl;
		tempPc[0]=pcDiPai;
		tempPl[0]=plDiPai;
		for(i=0;i<4;i++)
		{
			tempPc[i+1]=pcCard[i];
			tempPl[i+1]=plCard[i];
		}
		for(i=0;i<4;i++)//将牌按从大到小排序，冒泡排序法
			for(j=0;j<5-i-1;j++)
			{
				Card temp;
				if(tempPc[j].getNum()<tempPc[j+1].getNum() || (tempPc[j].getNum()==tempPc[j+1].getNum() && tempPc[j].getType()<tempPc[j+1].getType()))
				{
					temp=tempPc[j];
					tempPc[j]=tempPc[j+1];
					tempPc[j+1]=temp;
				}
				if(tempPl[j].getNum()<tempPl[j+1].getNum() || (tempPl[j].getNum()==tempPl[j+1].getNum() && tempPl[j].getType()<tempPl[j+1].getType()))
				{
					temp=tempPl[j];
					tempPl[j]=tempPl[j+1];
					tempPl[j+1]=temp;
				}
			}
		maxPc=tempPc[0];
		maxPl=tempPl[0];
		for(i=0;i<5;i++)//获取每组牌的的信息，包括相同的花色，相同的面值
			for(j=0;j<5;j++)
			{
				if (tempPc[i].getNum()==tempPc[j].getNum())
					tempPc[i].addCounte();
				if (tempPl[i].getNum()==tempPl[j].getNum())
					tempPl[i].addCounte();
				if (tempPc[i].getType()==tempPc[j].getType())
					tempPc[i].addColor();
				if (tempPl[i].getType()==tempPl[j].getType())
					tempPl[i].addColor();
					
			}
		rankPl=setRank(tempPl);
		rankPc=setRank(tempPc);
		pcDiPai=tempPc[0];
		plDiPai=tempPl[0];
		for(i=0;i<4;i++)
		{
			pcCard[i]=tempPc[i+1];
			plCard[i]=tempPl[i+1];
		}
		if(rankPl==rankPc)
		{
			switch(rankPl)
			{
				case 1:
					if(maxPl.getColor()!=4)
						maxPl=tempPl[2];
					if(maxPc.getColor()!=4)
						maxPc=tempPl[2];
					break;
				case 2:case 5:
					for(i=4;i>0;i--)
					{
						if(tempPl[i].getCounte()==3)
							maxPl=tempPl[i];
						if(tempPc[i].getCounte()==3)
							maxPc=tempPc[i];
					}
					break;
				case 3:
					int k;
					for(k=0;k<5 && tempPl[k]==tempPc[k];k++);
					if (k==4)
						return tempPl[0].getType()>tempPc[0].getType();
					else
						return 	tempPl[k].getNum()>tempPc[k].getNum();
				case 6:
					int doubNPl=0,doubNPc=0;
					Card maxDuiPc=null,minDuiPc=null;
					Card maxDuiPl=null,minDuiPl=null;
					for(i=0;i<4;i++)//找寻2对中的最小对和最大对
					{
						if(tempPl[i].getColor()==2)
						{
							doubNPl++;
							if(doubNPl==1)
								maxDuiPl=tempPl[i];
							if(doubNPl==3)
								minDuiPl=tempPl[i];
						}
							
						if(tempPc[i].getColor()==2)
						{
							doubNPc++;
							if(doubNPc==1)
								maxDuiPc=tempPc[i];
							if(doubNPc==3)
								minDuiPc=tempPc[i];
						}
					}
					if(maxDuiPl.getNum()==maxDuiPc.getNum())
					{
						if(minDuiPc.getNum() == minDuiPl.getNum())
							return (maxDuiPl.getType()>maxDuiPc.getType());
						else
							return minDuiPc.getNum() > minDuiPl.getNum();
					}else
						return maxDuiPl.getNum()>maxDuiPc.getNum();
					
				case 7:
					for(i=4;i>0;i--)
					{
						if(tempPl[i].getCounte()==2)
							maxPl=tempPl[i];
						if(tempPc[i].getCounte()==2)
							maxPc=tempPc[i];
					}
					break;
			}
			if (maxPl.getNum()==maxPc.getNum())
				return maxPl.getType()>maxPc.getType();
			else
				return (maxPl.getNum()>maxPc.getNum());
		}else
			return (rankPl<rankPc);
					
	}
	public boolean isShunZi(Card[] card)
	{
		for(int i=0;i<4;i++)
			if((card[i].getNum()-card[i+1].getNum())!=1)
				return false;
		return true;
				
	}
	public int setRank(Card[] card)
	{
		int doubN=0;
		int thirN=0;
		for(int i=0;i<5;i++)
		{
			if(card[i].getCounte()==3)
				thirN++;
			if(card[i].getCounte()==2)
				doubN++;
		}
		if((card[0].getColor()==5)&& isShunZi(card))//判断是否为同花顺
			return 0;
		if(card[0].getCounte()==4 || card[1].getCounte()==4)//判断是否为铁支
			return 1;
		if(thirN==3&&doubN==2)//判断葫芦
			return 2;
		if((card[0].getColor()==5))//判断同花
			return 3;
		if(isShunZi(card))//判断顺子
			return 4;
		if(thirN==3 && doubN==0)//判断三条
			return 5;
		if(doubN==4)//判断2对
			return 6;
		if(doubN==2)//判断1对
			return 7;
		return 8;
	}
	public void showCard(int round)
	{
		System.out.println("第"+round+"轮");
		if(round==5)
		{
			String[] name={"电脑 ","玩家 "};
			System.out.print( name[0]+CardType[pcDiPai.getType()]+CardNum[pcDiPai.getNum()]+"  ");
			for(int i=0;i<=round-2;i++)
			System.out.print(CardType[(pcCard[i].getType())]+CardNum[pcCard[i].getNum()]+"  ");
			System.out.print("\n");
			System.out.print( name[1]+CardType[plDiPai.getType()]+CardNum[plDiPai.getNum()]+"  ");
			for(int i=0;i<=round-2;i++)
				System.out.print(CardType[(plCard[i].getType())]+CardNum[plCard[i].getNum()]+"  ");
			System.out.print("\n");
		}else
		{
			String[] name={"电脑 ","玩家 "};
			System.out.print( name[0]+" XX"+"XX"+"  ");
			for(int i=0;i<=round-2;i++)
			System.out.print(CardType[(pcCard[i].getType())]+CardNum[pcCard[i].getNum()]+"  ");
			System.out.print("\n");
			System.out.print(name[0]+" XX"+"XX"+"  ");
			for(int i=0;i<=round-2;i++)
				System.out.print(CardType[(plCard[i].getType())]+CardNum[plCard[i].getNum()]+"  ");
			System.out.print("\n");
		}
		
	}
}
class Player
{
//	public int money=10000;//电脑的总赌注
	public int cardRece=0;//电脑拿到的五张牌
	private Card[] pcCard=new Card[5];//电脑玩家的五张牌
	public Player(){}
	public boolean follow(Card[] competorCard,int num)//根据对手的牌数组competorCard和牌数num，综合比较自己的牌，得出跟与不跟的结论
	{
		Card[] myCard=new Card[num+1];
		for(int i=0;i<num+1;i++)
			myCard[i]=pcCard[i];
		return true;
	}

	/*
	public void addStake()
	{}
	*/
	public void receCard(Card card)
	{
		this.pcCard[cardRece]=card;
		cardRece++;
	}
	public Card getCard(int index)
	{
		if((index>4)||(index<0))
			return null;
		else
			return pcCard[index];
	}
}
class Card
{
	private int type;//0-3分别代表着方块，梅花，红桃，黑桃
	private int num;//0-6分别代表着8，9，10，J，Q，K，A对于的牌面值
	private boolean status=false;//代表这张牌是明牌（牌面朝上），false代表这张牌是暗牌（牌面朝下）
	private int counte=0;//记录着牌面值为num的牌数（包括自己），用于比较“三条子”和“对子” 四条相同
	private int color=0;//记录这花色和自己相同的牌的数目，用于方便的比较是否为同花
//	private boolean choosed=false;//true 代表是已经被发出了,false代表还还未被发出
	public Card(){}
	public Card(int type,int num)
	{
		this.type=type;
		this.num=num;
	}
	public int getType()
	{
		return type;
	}
	public int getNum()
	{
		return num;
	}
	public boolean getStatus()
	{
		return status;
	}
	public void setStatus(boolean s)
	{
		status=s;
	}
	public int getCounte()//知道这张牌有多少是面值相同的牌
	{
		return counte;
	}
	public boolean addCounte()
	{
		if((counte>=0)&&(counte<4))//顶多有四张牌面值相同
		{
			counte++;
			return true;
		}else
			return false;
			
	}
/*	public boolean getChoosed()
	{
		return choosed;
	}

	public void setChoosed(boolean s)
	{
		choosed=s;
	}
*/
	public int getColor()
	{
		return color;
	}
	public void addColor()
	{
		color++;
	}
	
	
}
