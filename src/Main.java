import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends JFrame implements MouseListener {
    private Render render;
    private LogPanel actionLog;
    private ControlPanel fightUI;
    private Enemi enemi1;
    private Character perso1;
    private String playerAction;
    private String cpuAction = "attaque";
    private boolean fin = true;
    private int state=0;
    private int state2=0;
    
    

    public Main(){
        initUI();
    }
    private void initUI(){
        perso1 = new Character(280, 250);
        perso1.addItem(new Item("Potion", null, perso1, "PV", "+", 100));
        perso1.addItem(new Item("Potion", null, perso1, "PV", "+", 100));
        perso1.addItem(new Item("Potion", null, perso1, "PV", "+", 100));
        perso1.addItem(new Item("Potion", null, perso1, "PV", "+", 100));
        perso1.addItem(new Item("Potion", null, perso1, "PV", "+", 100));
        perso1.addItem(new Item("Haute Potion", null, perso1, "PV", "+", 400));
        perso1.addItem(new Item("Super Potion", null, perso1, "PV", "+", perso1.getPV_max()));
        perso1.addItem(new Item("Mana Plus", null, perso1, "MNA", "%", 20));
        perso1.addItem(new Item("Mana Plus", null, perso1, "MNA", "%", 20));
        perso1.addItem(new Item("Mana Plus", null, perso1, "MNA", "%", 20));
        perso1.addItem(new Item("Esprit supérieur", null, perso1, "MNA", "%", 50));
        perso1.addItem(new Item("Esprit supérieur", null, perso1, "MNA", "%", 50));
        perso1.addItem(new Item("Esprit supérieur", null, perso1, "MNA", "%", 50));
        perso1.addItem(new Item("Mana Prism", null, perso1, "MNA", "%", 100));
        enemi1 = new Enemi(480, 250, perso1.getNiveau());
        fightUI = new ControlPanel(0.3f, 1f, perso1, enemi1, this);
        actionLog = new LogPanel(0.5f, 1.0f);
        render = new Render();
        render.setLayout(null);
        add(render);

        render.addEntity(perso1);
        render.addEntity(enemi1);
        setTitle("RPG");
        setSize(800, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fightUI.setPersonnage(perso1);
        render.add(fightUI);
        render.add(actionLog);
        render.addMouseListener(this);
    }

    public static void main(String[] args){
        System.setProperty("file.encoding", "UTF-8");
        EventQueue.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }

    public void mouseClicked(MouseEvent e){
        System.out.println("clic");
        if(fin == false) {
        	int a,b;
        	a = getstate();
        	b = getstate2();
        	System.out.println(a+" "+b);
        	fin = true;
            tour(a,b);
        }
    }
    public void tour(int sta,int sta2){

        if (fin) {
            fin = false;
            fightUI.setVisible(false);

            if (perso1.getPV() > 0 && enemi1.getPV() > 0) {

                //Attack perso
                if (playerAction.equals("attaque")) {
                    if (sta == 0 && sta2 == 0) {
                        actionLog.updateLog(perso1.getNom() + " attaque !");
                        perso1.attaque(enemi1);
                        setstate(0, 1);
                    }
                }

                //magie
                if (playerAction.equals("magie")) {
                    Magie magie = new Magie(perso1,enemi1);
                    setstate(0, 1);
                    perso1.setmagie("");
                    setPlayerAction("");
                }

                //objets
                if(playerAction.equals("objet")){
                    perso1.use();
                    setPlayerAction("");
                    setstate(0, 1);
                }
            }
            //-----------------------------------------------------------------
            // l'affichage des degats et verification si ennemie mort
            if (sta == 0 && sta2 == 1) {
                setstate(1, 0);
                actionLog.updateLog(perso1.getNom() + " inflige " + perso1.getADMG() + " DMG ? l'enemi");
                if (enemi1.getPV() <= 0) {
                    setstate(3, 2);
                }
            }
            //----------------------------------------------------------------- 
            //Attack enemie
            if (enemi1.getPV() > 0 && perso1.getPV() > 0) {
                if (cpuAction.equals("attaque")) {
                    //-----------------------------------------------------------------
                    //statue
                    if (sta == 1 && sta2 == 0) {
                        if (enemi1.getEtat() == 2) {
                            actionLog.updateLog("L'ennemie est gel?");
                            setstate(2, 0);
                            enemi1.touretat++;
                        }
                        if (enemi1.getEtat() == 3) {
                            int aleatoire = (int) (Math.random() * 100);
                            if (aleatoire < 30) {
                                actionLog.updateLog("L'ennemie est paralys?");
                                setstate(2, 0);
                            }
                            enemi1.touretat++;
                        }
                        // enemie sans etat attaque enemie
                        if (enemi1.getEtat() < 2) {
                            actionLog.updateLog("l'enemie attaque !");
                            enemi1.attaque(perso1);
                            setstate(1, 1);
                        }
                    }

                    //----------------------------------------------------------------------
                    //Attack de l'ennemie
                    if (sta == 1 && sta2 == 1) {
                        actionLog.updateLog("l'enemie a inflig? " + enemi1.getADMG() + " au joueur");
                        setstate(1, 2);
                    }
                    //--------------------------------------------------------------------
                    //verife etat brul?
                    if (sta == 1 && sta2 == 2) {
                        if (enemi1.getEtat() == 1) {
                            enemi1.setDMG(12);
                            actionLog.updateLog("L'ennemie Brule");
                            setstate(2, 0);
                            enemi1.touretat++;
                            if (enemi1.getPV() <= 0) {
                                setstate(3, 2);
                            }
                        }
                        // verifie si le joueur est mort ou non 
                        if (enemi1.getEtat() != 2 && perso1.getPV() > 0 && enemi1.getPV() > 0) {
                            setstate(2, 0);
                        }
                        if (perso1.getPV() <= 0) {
                            setstate(4, 0);
                        }
                    }
                }
            }
            //------------------------------------------
            // etat de fin de combat
            if (sta == 4 && sta2 == 0) {
                actionLog.updateLog("Game over");
                setstate(2, 1);
            }
            if (sta == 3 && sta2 == 2) {
                actionLog.updateLog("Gagn?");
                setstate(3, 1);
            }
            if (sta == 3 && sta2 == 1) {
                actionLog.updateLog("vous avez gagn? " + enemi1.getGivenEXP() + " Exp");
                perso1.setEXP(enemi1.givenEXP);
                setstate(3, 0);
            }
            if (sta == 3 && sta2 == 0 && perso1.niveau < perso1.niveausup) {
                actionLog.updateLog("Vous etes montez niveau " + perso1.niveausup);
                perso1.niveau = perso1.niveausup;
            }
            if (sta == 3 && sta2 == 0 && perso1.niveau == perso1.niveausup) {
                setstate(2, 1);
            }
            //------------------------------------------------------
            // fin de tour si pv > 0
            if (sta == 2 && sta2 == 0) {
                if (enemi1.touretat >= 3)
                    enemi1.setEtat(0);
                fin = true;
                actionLog.updateLog("C'est votre tour");
                fightUI.refresh();
                fightUI.setVisible(true);
                setstate(0, 0);
            }
            //-------------------------------------------------------
            // fin de tour si pv = 0
            if (sta == 2 && sta2 == 1) {
                if (enemi1.touretat >= 3)
                    enemi1.setEtat(0);
                fin = true;
                setstate(0, 0);
            }

        }
    }
    public void setPlayerAction(String action){this.playerAction = action;}
    public void setCpuAction(String action){this.cpuAction = action;}
    public void setstate(int state,int state2) {
    	this.state = state;
    	this.state2 = state2;
    }
    public int getstate() {
    	return state;
    }
    public int getstate2() {
    	return state2;
    }
    public boolean getfin() {return fin;}
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public Character getperso() {
    	return perso1;
    }
}
