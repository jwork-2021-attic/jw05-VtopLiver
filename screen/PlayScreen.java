/*
 * Copyright (C) 2015 Aeranythe Echosong
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package screen;

import world.*;
import asciiPanel.AsciiPanel;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

/**
 *
 * @author Aeranythe Echosong
 */
public class PlayScreen implements Screen {

    private World world;
    private Player player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private List<String> oldMessages;
    private int lastCode;
    private AsciiPanel ter;
    private boolean iswaterSki;
    private boolean isfireSki;

    public PlayScreen() {
        this.screenWidth = 80;
        this.screenHeight = 24;
        createWorld();
        this.messages = new ArrayList<String>();
        this.oldMessages = new ArrayList<String>();
        lastCode=0;
        CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);
    }

    private void createCreatures(CreatureFactory creatureFactory) {
        this.player = creatureFactory.newPlayer(this.messages);

        for (int i = 0; i < 8; i++) {
            creatureFactory.newFungus();
        }
        creatureFactory.newBoss();
    }

    private void createWorld() {
        world = new WorldBuilder(90, 31).makeCaves().build();
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        // Show terrain
        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < screenHeight; y++) {
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy)) {
                    terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
                } else {
                    terminal.write(world.glyph(wx, wy), x, y, Color.DARK_GRAY);
                }
            }
        }
        // Show creatures
        for (Creature creature : world.getCreatures()) {
            if (creature.x() >= left && creature.x() < left + screenWidth && creature.y() >= top
                    && creature.y() < top + screenHeight) {
                if (player.canSee(creature.x(), creature.y())) {
                    terminal.write(creature.glyph(), creature.x() - left, creature.y() - top, creature.color());
                }
            }
        }
        // Creatures can choose their next action now
        world.update();
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages) {
        int top = this.screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++) {
            terminal.write(messages.get(i), 1, top + i + 1);
        }
        this.oldMessages.addAll(messages);
        messages.clear();
        ter=terminal;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        // Terrain and creatures
        displayTiles(terminal, getScrollX(), getScrollY());
        // Player
        terminal.write(player.glyph(), player.x() - getScrollX(), player.y() - getScrollY(), player.color());
        // Stats
        String stats = String.format("%3d/%3d hp", player.hp(), player.maxHP());
        terminal.write(stats, 1, 23);
        // Messages
        displayMessages(terminal, this.messages);
        displaywaterSki(terminal);
        displayfireSki(terminal);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                lastCode=2;
                player.moveBy(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                lastCode=3;
                player.moveBy(1, 0);
                break;
            case KeyEvent.VK_UP:
                lastCode=0;
                player.moveBy(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                lastCode=1;
                player.moveBy(0, 1);
                break;
            case KeyEvent.VK_A:
                iswaterSki=true;
                break;
                case KeyEvent.VK_S:
                isfireSki=true;
                break;
            
        }
        if(player.hp()<1){
            return new LoseScreen();
        }
        return this;
        
            
    }
    public void displaywaterSki(AsciiPanel ter){
        if(iswaterSki){
            player.waterSki(lastCode);
            for(int i=0;i<player.Lx.size();i++){
                ter.write("*",player.Lx.get(i)- getScrollX(),player.Ly.get(i)- getScrollY(),Color.BLUE);
                //System.out.println("waterSkiOK");
            }
            iswaterSki=false;
        }
    }
    public void displayfireSki(AsciiPanel ter){
        if(isfireSki){
            player.fireSki(lastCode);
            for(int i=0;i<player.Lx.size();i++){
                ter.write("*",player.Lx.get(i)- getScrollX(),player.Ly.get(i)- getScrollY(),Color.red);
                //System.out.println("waterSkiOK");
            }
            isfireSki=false;
        }
    }
    public int getScrollX() {
        return Math.max(0, Math.min(player.x() - screenWidth / 2, world.width() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y() - screenHeight / 2, world.height() - screenHeight));
    }

}
