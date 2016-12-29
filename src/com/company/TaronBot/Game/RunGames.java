package com.company.TaronBot.Game;

import com.company.TaronBot.Network.TakNetwork;
import org.apache.commons.math3.ml.neuralnet.Network;
import tech.deef.Tools.StaticGlobals;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sarnowskit on 12/28/2016.
 */
public class RunGames {
    List<TakNetwork> player1;
    List<TakNetwork> player2;
    int count;
    public RunGames(){
        player1=new LinkedList<>();
        player2=new LinkedList<>();
    }
    public boolean addGame(TakNetwork one, TakNetwork two){
        player1.add(one);
        player2.add(two);
        return true;
    }
    public void playGames(int cores){
        Iterator<TakNetwork> player1=this.player1.iterator();
        Iterator<TakNetwork> player2=this.player2.iterator();
        while(player1.hasNext()&&player2.hasNext()){
            TakNetwork one=player1.next();
            TakNetwork two=player2.next();
            Thread t = new Thread(){

                @Override
                public void run() {
                    while(count> cores)
                    count++;
                        if(one.getWidth()==two.getWidth()) {
                            int result=Board.playGame(one,two,one.getWidth());
                            if(result>0){
                                one.addWins();
                                two.addLosses();

                            }else if(result<0) {
                                two.addWins();
                                one.addLosses();

                            }

                        }
                    count--;
                    notify();
                }

            };
            while(count>=cores){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            t.start();

        }
    }
}
