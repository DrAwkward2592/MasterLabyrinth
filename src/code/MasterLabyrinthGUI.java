package code;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import code.Observer;

/**
 * Graphical User Interface for our Master Labyrinth board. As of now, shows a
 * visual representation of the tiles on the board. Paths on individual tiles
 * are shown in BLACK(representing the tiles "directional" variables.
 * 
 * @author Matt, Nick, team112
 *
 */
public class MasterLabyrinthGUI implements Runnable, Observer {
	private JPanel _boardPanel, _dataPanel, _gamePanel;
	private JFrame _window;
	private Board _board;
	private Tile _freeTile;
	private JPanel _freeTilePanel;
	private ArrayList<JButton> _playerButtons;
	private JPanel _movePanel;
	private JButton _moveButton;
	private JButton _passButton;
	private ArrayList<JButton> _shiftButtons;
	private JPanel _freePanel;
	private ArrayList<JTextField> _playerscores;

	/**
	 * Constructor.
	 * 
	 * @param b MasterLabyrinth Board obj
	 */
	public MasterLabyrinthGUI(Board b) {
		_board = b;
		_board.setObserver(this);
	}

	/**
	 * Function required as MasterLabyrinthGUI implements Runnable
	 */
	@Override
	public void run() {
		_window = new JFrame("Master Labyrinth");
		_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		_freeTile = _board.getFreeTile();
		_dataPanel = new JPanel();
		_dataPanel.setLayout(new GridLayout(3,1));
		_freeTilePanel = new JPanel();
		initializeBoard();
		initializeGamePanel();
		initializeData();


		_window.setLayout(new GridLayout(1, 2));
		_window.add(_gamePanel);
		//_window.add(_boardPanel);
		_window.add(_dataPanel);
		_window.pack();
		_window.setVisible(true);
		update();
	}
	/**
	 * This method initializes the game panel. The game panel consists of the 
	 * board in between, and shift buttons surrounding it. 
	 * This is a border layout, as opposed to a Gridlayout.
	 */

	private void initializeGamePanel(){
		_gamePanel = new JPanel();
		_gamePanel.setLayout(new BorderLayout());
		_gamePanel.add(_boardPanel, BorderLayout.CENTER);

		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		top.setLayout(new GridLayout(1,7));
		bottom.setLayout(new GridLayout(1,7));
		_shiftButtons = new ArrayList<>();
		for(int i=0;i<7;i++){

			if(i%2 != 0){
				JButton topShift = new JButton("Shift");
				JButton bottomShift = new JButton("Shift");
				top.add(topShift);
				bottom.add(bottomShift);
				topShift.addActionListener(new ShiftButtonListener(_board,i,true,false));
				bottomShift.addActionListener(new ShiftButtonListener(_board,i,false,false));
				_shiftButtons.add(topShift);
				_shiftButtons.add(bottomShift);
			}

			else{
				top.add(new JPanel());
				bottom.add(new JPanel());
			}	
		}

		JPanel left = new JPanel();
		JPanel right = new JPanel();
		left.setLayout(new GridLayout(7,1));
		right.setLayout(new GridLayout(7,1));
		for(int i=0;i<7;i++){
			if(i%2 != 0){
				JButton rightShift = new JButton("Shift");
				JButton leftShift = new JButton("Shift");
				right.add(rightShift);
				left.add(leftShift);
				rightShift.addActionListener(new ShiftButtonListener(_board,6-i,true,true));
				leftShift.addActionListener(new ShiftButtonListener(_board,6-i,false,true));
				_shiftButtons.add(rightShift);
				_shiftButtons.add(leftShift);
			}

			else{
				right.add(new JPanel());
				left.add(new JPanel());
			}

		}

		_gamePanel.add(top, BorderLayout.NORTH);
		_gamePanel.add(bottom, BorderLayout.SOUTH);
		_gamePanel.add(right, BorderLayout.EAST);
		_gamePanel.add(left, BorderLayout.WEST);
	}


	/**
	 * Sets up a 7x7 grid of JPanels which will hold visual representations of
	 * Tiles. Each JPanel is a 3x3 grid which will hold visual representation of
	 * a Tile's NEWS values. These sub-panels are labeled 0-8 as shown: 0 1 2 3
	 * 4 5 6 7 8 where sub-panel 1 should represent the Tile's North value, 5
	 * represents the Tile's East value, etc... sub-panel 4 will always be black
	 */
	private void initializeBoard() {
		_boardPanel = new JPanel();
		_boardPanel.setLayout(new GridLayout(Board.WIDTH, Board.HEIGHT));
		_boardPanel.setFocusable(false);

		for (int c = 0; c < Board.WIDTH; c++) {
			for (int r = 0; r < Board.HEIGHT; r++) {
				JPanel p = new JPanel();
				p.setBorder(new LineBorder(Color.WHITE));
				p.setLayout(new GridLayout(3, 3));
				p.setOpaque(true);
				p.setFocusable(false);
				p.setPreferredSize(new Dimension(60, 60));

				for (int i = 0; i < 3; i++) {// 3x3 grid on face of tile
					for (int j = 0; j < 3; j++) {
						JPanel pan = new JPanel();
						pan.setOpaque(true);
						pan.setPreferredSize(new Dimension(20, 20));
						pan.setBackground(Color.BLACK);
						pan.add(new JLabel());//each subpanel has a JLabel on it. Access it via (JLabel)getComponent(0) in update()
						p.add(pan);
					}

				}
				_boardPanel.add(p);
			}
		}
		_window.setFocusable(true);
		_window.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
					_board.movePlayer(_board.getCurrentPlayer(), e);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * Method to initilialize the data panel.
	 * The data panel contains information related to player movement, player score, and the state of the free tile.
	 */
	public void initializeData() {
		_playerButtons = new ArrayList<>();
		_playerscores = new ArrayList<>();
		JPanel playerbuttonpanel = new JPanel();
		playerbuttonpanel.setLayout(new GridLayout(2,4));
		for (int i = 0; i < _board.numberOfPlayers(); i++) {
			Player p = _board.getPlayer(i);
			JButton playerbutton = new JButton(p.getPlayerName());
			playerbutton.setBackground(p.getColor());
			_playerButtons.add(playerbutton);
			playerbuttonpanel.add(_playerButtons.get(i));
		}
		for (int i = 0; i < _board.numberOfPlayers(); i++) {
			Player p = _board.getPlayer(i);
			JTextField playerscore = new JTextField(4);
			playerscore.setEnabled(false);
			playerscore.setText("Score: "+p.getScore());
			_playerscores.add(playerscore);
			playerbuttonpanel.add(playerscore);
		}

		_dataPanel.add(playerbuttonpanel);
		JButton rotateTile = new JButton("Rotate tile");
		_freePanel = new JPanel();
		_freePanel.setLayout(new FlowLayout());
		_freePanel.add(rotateTile);
		rotateTile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_board.rotateFreeTile();

			}
		});

		_freePanel.add(_freeTilePanel);
		_dataPanel.add(_freePanel);

		_freeTilePanel.setLayout(new GridLayout(3, 3));
		for (int i = 0; i < 3; i++) {// 3x3 grid on face of tile
			for (int j = 0; j < 3; j++) {
				JPanel pan = new JPanel();
				pan.setOpaque(true);
				pan.setPreferredSize(new Dimension(20, 20));
				pan.setBackground(Color.BLACK);

				_freeTilePanel.add(pan);
			}
		}

		// setting center sub-panel to black (component 4 is center sub-panel)
		JPanel pan = (JPanel) _freeTilePanel.getComponent(4);
		pan.setBackground(Color.ORANGE);

		// setting north color based on tile's getNorth() value
		pan = (JPanel) _freeTilePanel.getComponent(1);// see javadoc for why
		// these are 1,5,7,3
		if (_freeTile.getNorth() == true) {
			pan.setBackground(Color.ORANGE);
		} else {
			pan.setBackground(Color.BLACK);
		}
		// setting east color based on tile's getEast() value
		pan = (JPanel) _freeTilePanel.getComponent(5);
		if (_freeTile.getEast() == true) {
			pan.setBackground(Color.ORANGE);
		} else {
			pan.setBackground(Color.BLACK);
		}
		// setting south color based on tile's getSouth() value
		pan = (JPanel) _freeTilePanel.getComponent(7);
		if (_freeTile.getSouth() == true) {
			pan.setBackground(Color.ORANGE);
		} else {
			pan.setBackground(Color.BLACK);
		}
		// setting west color based on tile's getWest() value
		pan = (JPanel) _freeTilePanel.getComponent(3);
		if (_freeTile.getWest() == true) {
			pan.setBackground(Color.ORANGE);
		} else {
			pan.setBackground(Color.BLACK);
		}


		JTextField textx = new JTextField(5);
		JTextField texty = new JTextField(5);

		_movePanel = new JPanel();
		_moveButton = new JButton("Move");
		_passButton = new JButton("Pass");

		_moveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_board.findPath();
				try{
					_board.movePlayer(Integer.valueOf(textx.getText()), Integer.valueOf(texty.getText()));
				}
				catch(NumberFormatException exception){
					System.out.println("Please enter numbers only!");
				}
				catch(IndexOutOfBoundsException index){
					System.out.println("Please enter valid indices (0-6)");
				}
			};
		});

		_passButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_board.switchPlayer();
			};
		});
		_movePanel.add(textx);
		_movePanel.add(texty);
		_movePanel.add(_moveButton);
		_movePanel.add(_passButton);

		_dataPanel.add(_movePanel);



	}
	/**
	 * Redraws the free tile on the data panel.
	 */
	private void redrawTile() {
		JPanel tile = _freeTilePanel;
		_freeTile = _board.getFreeTile();

		// setting center sub-panel to black (component 4 is center sub-panel)
		JPanel pan = (JPanel) tile.getComponent(4);
		pan.setBackground(Color.ORANGE);

		// setting north color based on tile's getNorth() value
		pan = (JPanel) tile.getComponent(1);// see javadoc for why these are
		// 1,5,7,3
		if (_freeTile.getNorth() == true) {
			pan.setBackground(Color.ORANGE);
		} else {
			pan.setBackground(Color.BLACK);
		}
		// setting east color based on tile's getEast() value
		pan = (JPanel) tile.getComponent(5);
		if (_freeTile.getEast() == true) {
			pan.setBackground(Color.ORANGE);
		} else {
			pan.setBackground(Color.BLACK);
		}
		// setting south color based on tile's getSouth() value
		pan = (JPanel) tile.getComponent(7);
		if (_freeTile.getSouth() == true) {
			pan.setBackground(Color.ORANGE);
		} else {
			pan.setBackground(Color.BLACK);
		}
		// setting west color based on tile's getWest() value
		pan = (JPanel) tile.getComponent(3);
		if (_freeTile.getWest() == true) {
			pan.setBackground(Color.ORANGE);
		} else {
			pan.setBackground(Color.BLACK);
		}
	}

	/**
	 * The Update method is required as this implements the Observer interface.
	 * Updates the visual representation of the Board.
	 * Will be split up into helper methods to keep it tidy.
	 */
	@Override
	public void update() {
		//System.out.println(""+_board.getPlayer(0).getX()+_board.getPlayer(0).getX());
		for (int c = 0; c < Board.WIDTH; c++) {
			for (int r = 0; r < Board.HEIGHT; r++) {
				JPanel p = (JPanel) _boardPanel.getComponent(r * Board.WIDTH + c);

				//JLabel pos = new JLabel("" + c + "," + (6 - r));
				//pos.setForeground(Color.WHITE);

				Tile t = _board.getTile(c, 6 - r);// 6-r because our 0,0 is at
				// bottom left instead of
				// top-left

				// setting center sub-panel to black (component 4 is center
				// sub-panel)
				JPanel pan = (JPanel) p.getComponent(4);
				pan.setBackground(Color.ORANGE);

				//pan.add(pos);

				// setting north color based on tile's getNorth() value
				pan = (JPanel) p.getComponent(1);// see javadoc for why these
				// are 1,5,7,3
				if (t.getNorth() == true) {
					pan.setBackground(Color.ORANGE);
				} else {
					pan.setBackground(Color.BLACK);
				}
				// setting east color based on tile's getEast() value
				pan = (JPanel) p.getComponent(5);
				if (t.getEast() == true) {
					pan.setBackground(Color.ORANGE);
				} else {
					pan.setBackground(Color.BLACK);
				}
				// setting south color based on tile's getSouth() value
				pan = (JPanel) p.getComponent(7);
				if (t.getSouth() == true) {
					pan.setBackground(Color.ORANGE);
				} else {
					pan.setBackground(Color.BLACK);
				}
				// setting west color based on tile's getWest() value
				pan = (JPanel) p.getComponent(3);
				if (t.getWest() == true) {
					pan.setBackground(Color.ORANGE);
				} else {
					pan.setBackground(Color.BLACK);
				}


				if(t.hasToken()){//if this tile has a token on it
					Token token = t.getToken();
					pan = (JPanel) p.getComponent(4);
					pan.setBackground(Color.PINK);
					if(token.isFacedDown()){
						//System.out.println("??");
						JLabel j = (JLabel)pan.getComponent(0);
						j.setText("??");
					}
					else{
						JLabel j = (JLabel)pan.getComponent(0);
						j.setText(token.getTokenNumber()+"");
					}
				}
				else{
					pan = (JPanel) p.getComponent(4);
					JLabel j = (JLabel)pan.getComponent(0);
					j.setText("");
				}
				if(t.hasPlayer()){//if this tile has at least 1 player on it
					if(t.getPlayerList().size()>=1){//tile has at least one player
						pan = (JPanel) p.getComponent(0);
						pan.setBackground(t.getPlayerList().get(0).getColor());
						pan = (JPanel) p.getComponent(2);
						pan.setBackground(Color.BLACK);
						pan = (JPanel) p.getComponent(6);
						pan.setBackground(Color.BLACK);
						pan = (JPanel) p.getComponent(8);
						pan.setBackground(Color.BLACK);
					}
					if(t.getPlayerList().size()>=2){//tile has at least two players on it
						pan = (JPanel)p.getComponent(2);
						pan.setBackground(t.getPlayerList().get(1).getColor());
						pan = (JPanel) p.getComponent(6);
						pan.setBackground(Color.BLACK);
						pan = (JPanel) p.getComponent(8);
						pan.setBackground(Color.BLACK);
					}
					if(t.getPlayerList().size()>=3){//tile has at least three players on it
						pan = (JPanel)p.getComponent(6);
						pan.setBackground(t.getPlayerList().get(2).getColor());
						pan = (JPanel) p.getComponent(8);
						pan.setBackground(Color.BLACK);
					}
					if(t.getPlayerList().size()==4){//tile has 4 players on it
						pan = (JPanel)p.getComponent(8);
						pan.setBackground(t.getPlayerList().get(3).getColor());
					}



				}
				else{
					pan = (JPanel) p.getComponent(0);
					pan.setBackground(Color.BLACK);
					pan = (JPanel) p.getComponent(2);
					pan.setBackground(Color.BLACK);
					pan = (JPanel) p.getComponent(6);
					pan.setBackground(Color.BLACK);
					pan = (JPanel) p.getComponent(8);
					pan.setBackground(Color.BLACK);

				}
			}
		}
		redrawTile();
		Path pathsFile = new Path(this);
		pathsFile.updatePaths();
		
		System.out.println(_board.getCurrentPlayer().getPlayerName());
		System.out.println(_board.getCurrentPlayer().getX());
		System.out.println(_board.getCurrentPlayer().getY());


		for(int i=0; i<_playerButtons.size();i++){
			JButton jb = _playerButtons.get(i);
			if(_board.getCurrentPlayer()==_board.getPlayer(i)){

				jb.setEnabled(true);
				jb.setBackground(_board.getPlayer(i).getColor());
				jb.setOpaque(true);
			}
			else{
				jb.setEnabled(false);
				jb.setBackground(_board.getPlayer(i).getColor());
				Color c = jb.getBackground().darker().darker();
				jb.setBackground(c);

			}	
		}

		//Only allows moving after shifting.  Enables move button if stage is 1, disables it if stage is 0.
		if(_board.getCurrentStage() ==0){
			_moveButton.setEnabled(false);
			_passButton.setEnabled(false);
		}
		else{
			_moveButton.setEnabled(true);
			_passButton.setEnabled(true);
		}

		//Allows a player to only shift once per turn.
		for(int i = 0;i<_shiftButtons.size();i++){
			JButton jb = _shiftButtons.get(i);
			if(_board.getCurrentStage()==0){
				jb.setEnabled(true);
			}
			else{
				jb.setEnabled(false);
			}
		}

		for(int i=0;i<_playerscores.size();i++){
			JTextField tf = _playerscores.get(i);
			tf.setText("Score: "+_board.getPlayer(i).getScore());
		}
	}
	/**
	 * Accessor for the Board Panel.
	 * @return the JPanel representing the 7x7 board.
	 */
	public JPanel getBoardPanel() {
		return _boardPanel;
	}

}
