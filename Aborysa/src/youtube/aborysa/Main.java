package youtube.aborysa;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ARBDrawBuffers;
import org.lwjgl.opengl.ARBDrawBuffersBlend;

import youtube.aborysa.game.Render.Texture.*;
import youtube.aborysa.game.Render.BlendMode;
import youtube.aborysa.game.Render.Drawer;
import youtube.aborysa.game.Render.RenderTexture;
import youtube.aborysa.game.Render.Screen;
import youtube.aborysa.game.Tiles.SheatSprite;
import youtube.aborysa.game.Tiles.Sprite;
import youtube.aborysa.game.Tiles.SpriteSheat;
import youtube.aborysa.game.Tiles.USprite;
import youtube.aborysa.game.Tiles.USpriteSheat;
import youtube.aborysa.game.Util.FPSCounter;
import youtube.aborysa.game.GameObjects.TestGround;
import youtube.aborysa.game.GameObjects.TestProject;
import youtube.aborysa.game.Input.KeyHandler;
import youtube.aborysa.game.Input.KeyListener;
import youtube.aborysa.game.Input.MouseHandler;
import youtube.aborysa.game.Input.MouseListener;
import youtube.aborysa.game.Math.geometrics.*;

public class Main implements KeyListener, MouseListener {
	static boolean left = false;
	static boolean up = false;
	static boolean down = false;
	static boolean right = false;
	static Point2f pos = new Point2f(0,0,false);
	static Texture tex;
	static ArrayList<TestProject> pros = new ArrayList<TestProject>();
	static ArrayList<TestProject> killed = new ArrayList<TestProject>();
	static int killCount = 0;
	public final static String Root = Main.class.getResource("../../").getPath();
	public final static URL testRoot = Main.class.getResource("../../natives");
	public static String absRoot;
	static Texture sheat1;
	static USpriteSheat sTest;
	static USprite testSprite;
	static USprite testSprite2;
	static USprite testSprite3;
	static USprite playerSpr;

	public static polyShape playerShape = new polyCircle(32,4, pos);
//	public static polyShape playerShape = new polyShape(new Point2f[]{new Point2f(0,32),new Point2f(32,32),new Point2f(32,0),new Point2f(0,0)}, pos);
//	public static polyShape obShap =new polyShape(new Point2f[]{new Point2f(0,128),new Point2f(64,128),new Point2f(64,32)}, new Point2f(64,128));
	public static polyCircle obShap =new polyCircle(64,6, new Point2f(128,128));
	
	public Main(){
		//Pointless constructor so that it is possible to init this shit
	}
	

	public static void main(String[] args) {
	
		Vector2f[] normals = obShap.getNormals(true);
		for(int i=0; i < normals.length;i++){
			System.out.println(normals[i]);
		}
		//System.exit(0);
		
		try {
			absRoot = new File(Root + "../").getCanonicalPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(absRoot);
		//System.exit(0);
		System.out.println(Root);
		System.out.println(testRoot);
		//System.out.println(new File("Aborysa.jar!/img/").getAbsolutePath());
		//System.exit(0);
		//System.out.println(Math.acos(-10/13));
		//System.out.println(Math.cos(Math.acos(-10/13)));
	//	System.exit(0);
		Point2f[] drawingPoints = new Point2f[]{
				new Point2f(0,0,false),new Point2f(180,-90,false),new Point2f(300,-35,false),
				new Point2f(300,-35,false),new Point2f(332,-15,false), new Point2f(180,0,false)		
		};

		//Point2f tPos = new Point2f(0,300,false);
		TestGround ground = new TestGround(drawingPoints,drawingPoints,pos);
		Triangle[] tri = ground.getMesh();
		System.out.println("Size: " + tri.length);
		for(int i=0; i< tri.length;i++){
				//System.out.println(tri[i].getPoints());
		}
		Vector2f vecTest = ground.getSurfaceVector(64, -32);
	//	System.out.println("X1: " + vecTest.getX() +", Y1:" + vecTest.getY() + ", X2: " + vecTest.getPos().getX() + ", Y2:" + vecTest.getPos().getY());
		
		//System.exit(0);
		System.setProperty("org.lwjgl.librarypath",new File("./natives").getAbsolutePath());
		//Canvas test = new Canvas();
		//Canvas test2 = new Canvas();
		//JFrame testFrame = new JFrame();
		//JFrame testFrame2 = new JFrame();
		
/*		
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.add(test);
		testFrame.setPreferredSize(new Dimension(640,480));
		testFrame.setVisible(true);
		test.setVisible(true);
	*/
		Screen.init(640,480,"TEST",null);
		
		//Screen.setCanvas(test);
		System.out.println(Root);
		//System.exit(0);
		KeyHandler.init();
		KeyHandler.addKeyListener(new Main());
		MouseHandler.init();
		MouseHandler.addMouseListener(new Main());
		tex = Screen.loadImage("./img/test.png");
		Texture t = Screen.loadImage("./img/Potet.png");
		Texture t2 = Screen.loadImage("./img/Potet_2.png");
		Texture t3 = Screen.loadImage("./img/Potet_3.png");
		sheat1 = Screen.loadImage("./img/SpriteSheet.png");
		playerSpr = new USprite(tex);
		sTest = new USpriteSheat(sheat1,32,32);
		testSprite = new USprite(sTest,0,2);
		testSprite.setImageIndex(1);
		testSprite2 = new USprite(sTest,1,2);
		testSprite3 = testSprite2.clone();
		FPSCounter counter = new FPSCounter();
		while (Screen.isRunning){
			
			KeyHandler.update();
			MouseHandler.update();
			Vector2f fixVec = polyShape.getCollision(playerShape,obShap);
			if (fixVec!=null){
				pos.setPos(playerShape.getPos().addPoint(new Point2f(fixVec.getX()/2,fixVec.getY()/2)));
				obShap.setPos(obShap.getPos().addPoint(new Point2f(-fixVec.getX()/2,-fixVec.getY()/2)));		
				Drawer.setColor(0f,1f,0f,1f);
				Drawer.drawPolygon(new Polygon(new Point2f[]{new Point2f(0,0),new Point2f(fixVec.getX()*15,fixVec.getY()*15),new Point2f(fixVec.getX()*10 + 12,fixVec.getY()*10),new Point2f(12,0)  }, pos.addPoint(new Point2f(16,16))));
			}
			//Screen.setColor(1f, 1f, 1f);
		//	Screen.setCanvas(test);
			Drawer.drawSprite(playerSpr,new Point2f(64,32,false));
			Drawer.drawSprite(playerSpr,new Point2f(96,32,false));
			Drawer.drawSprite(playerSpr,new Point2f(128,32,false));
			Drawer.setColor(1f,0,0,1f);
			//Drawer.drawFillRec(new Vector2f(32,32, new Point2f(100,100,false)));
			Drawer.setColor(1f,1f,1f,1f);
		//	Screen.drawImgStr(100, 100,32,32, t);
		//	Screen.drawImgStr(164, 100,32,32, t2);
		//	Screen.drawImgStr(228, 100,128,128, t3);
		//	Screen.run();
			//testSprite.advance();
		//	Screen.setCanvas(test2);
			Drawer.draw(new RenderTexture(new Vector2f(32,32,new Point2f(300,300,false)),testSprite.getTexCords(), sTest.getTexture()));
			for(int i=0; i<6;i++){
				for(int k=0;k<3;k++)
				{
					//Drawer.draw(new RenderTexture(new Vector2f(32,32,new Point2f(364 + k*32,300 + i*32,false)),testSprite2.getTexCords(), sTest.getTexture()));
					//Drawer.drawSprite(testSprite2, new Point2f(364 + k*32,300 + i*32,false));
					Drawer.drawSprite(sTest, testSprite2.getImageIndex() + testSprite2.getSindex(), 0, 364 +  k*32, 300 + i*32);
				}
				for(int k=3;k<6;k++)
					Drawer.draw(new RenderTexture(new Vector2f(32,32,new Point2f(364 + k*32,300 + i*32,false)),testSprite3.getTexCords(), sTest.getTexture()));
			}
			
			
			for(TestProject i : pros){
				i.update();
				if (i.killed){
					killed.add(i);
				}
			}
			Drawer.setColor(1f,1f,1f,0.9f);
			Drawer.setColor(1f,0,0,1f);
			Drawer.setBlendMode(BlendMode.BLEND_ONE);
			//Drawer.drawFillRec(new Vector2f(64,64,pos));
			Drawer.drawShape(obShap);
			//for(int i=0; i< tri.length;i++){
			//	Drawer.drawPolygon(tri[i]);
			//}
			Drawer.setBlendMode(BlendMode.BLEND_ALPHA);
			Drawer.setColor(1f,1f,1f,1f);
			Drawer.drawSprite(playerSpr,pos);
			Drawer.setColor(1, 0, 0, 1);
			Drawer.drawShape(playerShape);
			for(TestProject i : killed){
				pros.remove(i);
				killCount++;
			}
			killed.clear();
			killCount = 0;
			if(right){
				pos.setCords(pos.getX()+3,pos.getY());			
			}
			if(left){
				pos.setCords(pos.getX()-3,pos.getY());
			}
			if(up){
				pos.setCords(pos.getX(),pos.getY()-3);
			}
			if(down){
				pos.setCords(pos.getX(),pos.getY()+3);
			}
			//Screen.drawImageStr(228+64, 100,128,128, t3);
			//	System.out.println(KeyHandler.getKey()); 
			//Screen.setColor(1f,0f,0f);
			//Screen.setColor(1f,1f,1f);
			//Screen.drawImagePart(228+64, 100,0,0,64,64, p3);
			Screen.run();
			counter.tick();
		//	System.out.println("FPS: " + counter.getFPS());
		}
		System.out.println("Terminating");
		MouseHandler.dispose();
		KeyHandler.dispose();
		Screen.cleanUp();
	
		Point2f p1 = new Point2f(1,1,false);
		Point2f p2 = new Point2f(3,-1,false);
		Vector2f a = new Vector2f(2,2,p1);
		Vector2f b = new Vector2f(-5,1,p2);
		Point2f p3 = geo.getIntersection(a, b);
		if (p3 == null){
			System.out.println("Lines are not intersecting!");
		}else{
			System.out.println(p3.toString());
		}
	}

	@Override
	public void ButtonPressed(int keyCode) {
		if (keyCode == 200){
			up = true;
		}
		if (keyCode == 208){
			down = true;
		}
		if (keyCode == 203){
			left = true;
		}
		if (keyCode == 205){
			right = true;
		}
		if(keyCode == Keyboard.KEY_SPACE){
			Main.testSprite.advance();
			Main.testSprite2.advance();
			
		}
	}

	@Override
	public void ButtonReleasd(int keyCode) {
		if (keyCode == 200){
			up = false;
		}
		if (keyCode == 208){
			down = false;
		}
		if (keyCode == 203){
			left = false;
		}
		if (keyCode == 205){
			right = false;
		}		
	}
	@Override
	public void mouseClick(float x, float y, int code) {
		//System.out.println("Mouse pressed: " + x + ", " + y + ", " + code);
		float dx = x - Main.pos.getX();
		float dy = ((480-y) - Main.pos.getY());
		float xV = (float) (dx/Math.sqrt(Math.pow(dx,2) + Math.pow(dy, 2))+(Math.random()-0.5));
		float yV = (float) (dy/Math.sqrt(Math.pow(dx,2) + Math.pow(dy, 2))+(Math.random()-0.5));
		//System.out.println(xV);
		//System.out.println(yV);
		Point2f tempPos = pos.clone();
		pros.add(new TestProject(new Vector2f(xV*6,yV*6,tempPos), Main.playerSpr));
		
	}
	@Override
	public void mouseRelease(float x, float y, int code) {
		
	}
}
