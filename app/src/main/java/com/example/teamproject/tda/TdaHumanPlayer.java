package com.example.teamproject.tda;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import com.example.teamproject.R;
import com.example.teamproject.game.GameHumanPlayer;
import com.example.teamproject.game.GameMainActivity;
import com.example.teamproject.game.infoMsg.GameInfo;

public class TdaHumanPlayer extends GameHumanPlayer implements View.OnClickListener,View.OnTouchListener {

    // These variables will reference widgets that will be modified during play
    private TextView[] hoards = null;
    private TextView[] handSizes = null;

    private TextView deckSize = null;

    private ImageView[][] flights = null;
    private ImageView[] hand = null;
    private ArrayList<ImageView> board = null;


    private ImageView zoom = null;

    private ImageView[] antePile = null;

    private Button[] buttons = null;

    private TextView zoomStrength1 = null;
    private TextView zoomStrength2 = null;

    private TdaGameState tda;

    private GameMainActivity myActivity;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public TdaHumanPlayer(String name) {
        super(name);
        tda = new TdaGameState();
        handSizes = new TextView[3];
    }

    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.root);
    }

    @Override
    public void receiveInfo(GameInfo info) {

        boolean gameInfo = info instanceof TdaGameState;
        if(!gameInfo){
            super.flash(Color.RED,100);
        }
        if (info instanceof TdaGameState) {
            tda = (TdaGameState)info;

            //updating all hand sizes
            for (int i = 0; i < 3; i++) {
                handSizes[i].setText(Integer.toString(tda.getHandSize(i)));
            }

            //updating all hoard values
            for (int i = 0; i < 4; i++) {
                String currentHoard = Integer.toString(tda.getHoard(i));
                hoards[i].setText(currentHoard);
                System.out.println(currentHoard);
            }

            if(tda.getCurrentPlayer()==playerNum){
                buttons[0].setBackgroundColor(Color.GREEN);
            }


            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 3;j++){
                    String name = tda.getFlightCard(i,j).getName();
                    setImage(flights[i][j],name);
                    board.add(flights[i][j]);
                    flights[i][j].setOnTouchListener(this);
                }
            }

            for(int i = 0; i < 10; i++){
                String name = tda.getHandCard(i).getName();
                setImage(hand[i],name);
                board.add(hand[i]);
                hand[i].setOnTouchListener(this);
            }

        }

        //If its this human player's turn;
        if(tda.getCurrentPlayer()==playerNum){
            if(tda.getPlayButton()==true){
                buttons[0].setBackgroundColor(Color.GREEN);
            }
            else if(tda.getPlayButton()==false){
                buttons[0].setBackgroundColor(Color.GRAY);
            }
            buttons[2].setBackgroundColor(Color.GREEN);
            buttons[2].setText("END TURN");
        }


    }

    @Override
    public void setAsGui(GameMainActivity activity) {
        // remember the activity
        myActivity = activity;

        // Load the layout resource for our GUI
        activity.setContentView(R.layout.activity_main);

        //Initialize the widget reference member variables

        //hand sizes
        handSizes[0] = activity.findViewById(R.id.handSize1);
        handSizes[1] = activity.findViewById(R.id.handSize2);
        handSizes[2] = activity.findViewById(R.id.handSize3);

        hoards = new TextView[4];
        hoards[0] = activity.findViewById(R.id.hoardValue);
        hoards[1] = activity.findViewById(R.id.hoard1);
        hoards[2] = activity.findViewById(R.id.hoard2);
        hoards[3] = activity.findViewById(R.id.hoard3);

        flights = new ImageView[4][3];

        flights[0][0] = activity.findViewById(R.id.player0Flight0);
        flights[0][1] = activity.findViewById(R.id.player0Flight1);
        flights[0][2] = activity.findViewById(R.id.player0Flight2);

        flights[1][0] = activity.findViewById(R.id.player1Flight0);
        flights[1][1] = activity.findViewById(R.id.player1Flight1);
        flights[1][2] = activity.findViewById(R.id.player1Flight2);

        flights[2][0] = activity.findViewById(R.id.player2Flight0);
        flights[2][1] = activity.findViewById(R.id.player2Flight1);
        flights[2][2] = activity.findViewById(R.id.player2Flight2);

        flights[3][0] = activity.findViewById(R.id.player3Flight0);
        flights[3][1] = activity.findViewById(R.id.player3Flight1);
        flights[3][2] = activity.findViewById(R.id.player3Flight2);



        hand = new ImageView[10];
        hand[0] = activity.findViewById(R.id.playerHand1);
        hand[1] = activity.findViewById(R.id.playerHand2);
        hand[2] = activity.findViewById(R.id.playerHand3);
        hand[3] = activity.findViewById(R.id.playerHand4);
        hand[4] = activity.findViewById(R.id.playerHand5);
        hand[5] = activity.findViewById(R.id.playerHand6);
        hand[6] = activity.findViewById(R.id.playerHand7);
        hand[7] = activity.findViewById(R.id.playerHand8);
        hand[8] = activity.findViewById(R.id.playerHand9);
        hand[9] = activity.findViewById(R.id.playerHand10);

        board = new ArrayList<>();

        buttons = new Button[3];
        buttons[0] = activity.findViewById(R.id.selectCardPlay);
        buttons[1] = activity.findViewById(R.id.selectCardClose);
        buttons[2] = activity.findViewById(R.id.endTurnButton);

        for(Button b : buttons){
            b.setOnClickListener(this);
        }

        zoom = activity.findViewById(R.id.cardZoom);
        zoomStrength1 = activity.findViewById(R.id.zoomCardStrength1);
        zoomStrength2 = activity.findViewById(R.id.zoomCardStrength2);

    }

    public void setImage(ImageView iv, String name){
        switch (name) {

            case "Silver Dragon":
                iv.setImageResource(R.drawable.silverdragon);
                break;
            case "Copper Dragon":
                iv.setImageResource(R.drawable.copperdragon1);
                break;
            case "Red Dragon":
                iv.setImageResource(R.drawable.reddragon3);
                break;
            case "Gold Dragon":
                iv.setImageResource(R.drawable.golddragon6);
                break;
            case "Brass Dragon":
                iv.setImageResource(R.drawable.brassdragon9);
                break;
            case "Black Dragon":
                iv.setImageResource(R.drawable.blackdragon1);
                break;
            case "Blue Dragon":
                iv.setImageResource(R.drawable.bluedragon1);
                break;
            case "Bronze Dragon":
                iv.setImageResource(R.drawable.bronzedragon1);
                break;
            case "White Dragon":
                iv.setImageResource(R.drawable.whitedragon);
                break;
            case "Green Dragon":
                iv.setImageResource(R.drawable.greendragon);
                break;
            case "Bahamut":
                iv.setImageResource(R.drawable.bahamut);
                break;
            case "Dracolich":
                iv.setImageResource(R.drawable.dracolich);
                break;
            case "Tiamat":
                iv.setImageResource(R.drawable.tiamat);
                break;
            case "The Princess":
                iv.setImageResource(R.drawable.princess);
                break;
            case "The Fool":
                iv.setImageResource(R.drawable.fool);
                break;
            case "The Druid":
                iv.setImageResource(R.drawable.druid);
                break;
            case "The Archmage":
                iv.setImageResource(R.drawable.hermit);
                break;
            case "The Dragon Slayer":
                iv.setImageResource(R.drawable.dragonslayer);
                break;
            case "The Priest":
                iv.setImageResource(R.drawable.priest);
                break;
            case "The Thief":
                iv.setImageResource(R.drawable.thief);
                break;
            default:
                iv.setImageResource(R.drawable.cardback);
                break;

        }
    }
    @Override
    public void onClick(View view) {

        //close button pressed
        if(view == buttons[1]){
            zoom.setVisibility(View.INVISIBLE);
            zoomStrength1.setVisibility(View.INVISIBLE);
            zoomStrength2.setVisibility(View.INVISIBLE);
            buttons[0].setVisibility(View.INVISIBLE);
            buttons[1].setVisibility(View.INVISIBLE);

        }

        //play button is pressed
        if(view == buttons[0]){
            Card c = new Card(tda.getSelectedCard());
            super.game.sendAction(new TdaPlayCardAction(this));
            if(c.getName()!=null){
                board.remove(c);
            }
            zoom.setVisibility(View.INVISIBLE);
            zoomStrength1.setVisibility(View.INVISIBLE);
            zoomStrength2.setVisibility(View.INVISIBLE);
            buttons[0].setVisibility(View.INVISIBLE);
            buttons[1].setVisibility(View.INVISIBLE);
        }
        if(view == buttons[2]) {
            //super.game.sendAction(new TdaPlayCardAction(this));
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int x = (int)motionEvent.getX();
        int y = (int)motionEvent.getY();

         for(int i = 0; i<4;i++){
            for(int j = 0; j<3; j++){
              if(view == (flights[i][j])) {
                  String strength = Integer.toString(tda.getFlightCard(i,j).getStrength());
                  String name = tda.getFlightCard(i,j).getName();
                  setImage(zoom,name);
                  buttons[0].setVisibility(View.VISIBLE);
                  buttons[1].setVisibility(View.VISIBLE);
                  zoomStrength1.setText(strength);
                  zoomStrength2.setText(strength);
                  zoom.setVisibility(View.VISIBLE);
                  zoomStrength1.setVisibility(View.VISIBLE);
                  zoomStrength2.setVisibility(View.VISIBLE);
                  int index = board.indexOf(flights[i][j]);
                  super.game.sendAction(new TdaSelectCardAction(this,index));
              }
            }
         }

        for(int i = 0; i< tda.getHandSize(0); i++){
            if(view == hand[i]) {
                String strength = Integer.toString(tda.getHandCard(i).getStrength());
                String name = tda.getHandCard(i).getName();
                setImage(zoom,name);
                buttons[0].setVisibility(View.VISIBLE);
                buttons[1].setVisibility(View.VISIBLE);
                zoomStrength1.setText(strength);
                zoomStrength2.setText(strength);
                zoom.setVisibility(View.VISIBLE);
                zoomStrength1.setVisibility(View.VISIBLE);
                zoomStrength2.setVisibility(View.VISIBLE);
                int index = board.indexOf(hand[i]);
                super.game.sendAction(new TdaSelectCardAction(this,index));

            }
        }

        return true;
    }
}
