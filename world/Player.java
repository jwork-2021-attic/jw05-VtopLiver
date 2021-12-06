package world;
import java.awt.Color;
import java.util.*;
public class Player extends Creature{
    public Player(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius){
        super(world, glyph, color, maxHP, attack, defense, visionRadius);
        Lx=new ArrayList<Integer>();
        Ly=new ArrayList<Integer>();
    }
    @Override
    public void run(){
        try {
            Thread.sleep(1000);
        } 
        catch (Exception e) {}
    }
    public ArrayList<Integer> Lx;
    public ArrayList<Integer> Ly;
    public void waterSki(int lastCode){
        switch (lastCode) {
            case 0:
                OnSki(-1, 1, -3, -1);
                break;
            case 1:
                OnSki(-1, 1, 1, 3);
                break;
            case 2:
                OnSki(-3, -1, -1, 1);
                break;
            case 3:
                OnSki(1, 3, -1, 1);
                break;
        
            default:
                break;
        }
    }
    public void OnSki(int xs,int xe,int ys,int ye){
        Lx.clear();
        Ly.clear();
        for(int i=xs;i<=xe;i++){
            for(int j=ys;j<=ye;j++){
                if(canEnter(x()+i, y()+j)){
                    Creature c=world.creature(x()+i,y()+j);
                    Lx.add(x()+i);
                    Ly.add(y()+j);
                    if(c!=null){
                        this.attack(c);
                        
                    }
                }
            }
        }
    }
    public void fireSki(int lastCode){
        switch (lastCode) {
            case 0:
                OnSki(0, 0, -5, -1);
                break;
            case 1:
                OnSki(0, 0, 1, 5);
                break;
            case 2:
                OnSki(-5, -1, 0, 0);
                break;
            case 3:
                OnSki(1, 5, 0, 0);
                break;
        
            default:
                break;
        }
    }
}
