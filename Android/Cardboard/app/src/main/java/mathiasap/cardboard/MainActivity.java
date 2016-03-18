package mathiasap.cardboard;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;

import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;

import mathiasap.cardboard.matrix.Camera;
import mathiasap.cardboard.mesh.Mesh;
import mathiasap.cardboard.mesh.MeshInstance;
import mathiasap.cardboard.mesh.OBJLoader;
import mathiasap.cardboard.mesh.TexMesh;
import mathiasap.cardboard.misc.Key2D;
import mathiasap.cardboard.misc.Maths;
import mathiasap.cardboard.misc.Vektor3f;
import mathiasap.cardboard.rendering.LoadMesh;
import mathiasap.cardboard.rendering.Renderer;
import mathiasap.cardboard.shaders.AbstractShader;
import mathiasap.cardboard.shaders.FramebufferShader;
import mathiasap.cardboard.shaders.MeshShader;
import mathiasap.cardboard.shaders.TerrainShader;
import mathiasap.cardboard.terrain.Terrain;
import mathiasap.cardboard.terrain.TerrainHandlingThread;
import mathiasap.cardboard.terrain.TerrainMonitor;
import mathiasap.cardboard.textures.MeshTexture;
import mathiasap.cardboard.textures.TerrainMultiTexture;
import mathiasap.cardboard.textures.TerrainTexture;


public class MainActivity extends CardboardActivity implements CardboardView.StereoRenderer
{

    public static AssetManager assets;
    private CardboardOverlayView overlayView;
    private Renderer renderer;
    private Camera camera = new Camera();

    private MeshShader meshShader;
    private TerrainShader terrainShader;
    private FramebufferShader framebufferShader;

    private MeshInstance shrekIns;
    private TerrainMultiTexture terrainMultiTexture;


    private Map<Key2D,TerrainMonitor> terrainMonitorList=new HashMap<>();
    private TerrainHandlingThread terrainHandler= new TerrainHandlingThread(new Vektor3f(0,0,0),terrainMonitorList);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.common_ui);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRestoreGLStateEnabled(false);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);

        overlayView = (CardboardOverlayView) findViewById(R.id.overlay);





    }


    private void terrainGenerationCheck()
    {

        Vektor3f playerPos=new Vektor3f(0,0,0);
        int currentTerrainX=(int) Maths.floor(playerPos.x / Terrain.SIZE);
        int currentTerrainZ=(int) Maths.floor(playerPos.z / Terrain.SIZE);
        //System.out.println(currentTerrainX +"   "+ currentTerrainZ);

        int genRad=10;//10
        for(int i=-genRad;i<genRad+1;i++)
            for(int j=-genRad;j<genRad+1;j++)
                if(!terrainMonitorList.containsKey(new Key2D(currentTerrainX+i,currentTerrainZ+j)))
                {
                    Terrain ter= new Terrain(terrainMultiTexture,currentTerrainX+i,currentTerrainZ+j);
                    TerrainMonitor monTer= new TerrainMonitor(ter);

                    monTer.setLockedByGenThread(true);
                    terrainMonitorList.put( new Key2D(currentTerrainX+i,currentTerrainZ+j),monTer);
                    terrainHandler.addTerrainToQueue(monTer);
                }
    }
    private InputStreamReader getIS(int id)
    {
        return new InputStreamReader(getResources().openRawResource(id));
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onNewFrame(HeadTransform headTransform)
    {
        renderer.clearMeshInstances();
        camera.updateCamera(headTransform);
       // terrainGenerationCheck();

        terrainGenerationCheck();
        uploadDataToGPU();
        shrekIns.rotate(0,-10,0);
        shrekIns.move(-0, 0, -0);
        renderer.putInstance(shrekIns);


    }

    @Override
    public void onDrawEye(Eye eye)
    {
        camera.updateView(eye);
        terrainShader.useProgram();
        renderer.render(/*eye.getViewport().width,eye.getViewport().height*/);


    }

    private void uploadDataToGPU()
    {
        try
        {
            for(Iterator<Key2D> it=terrainMonitorList.keySet().iterator();it.hasNext();)
            {
                Key2D terrainMonitorKey=it.next();

                if(!terrainMonitorList.get(terrainMonitorKey).isLockedByGenThread()){
                    if(terrainMonitorList.get(terrainMonitorKey).isReadyToUpload())
                    {
                        terrainMonitorList.get(terrainMonitorKey).getTerrain().uploadMesh();
                        terrainMonitorList.get(terrainMonitorKey).setReadyToUpload(false);

                    }

                    if(Vektor3f.sub(terrainMonitorList.get(terrainMonitorKey).getTerrain().getPosition(), /*player.getPosition()*/new Vektor3f(0,0,0)).length()<15000 && terrainMonitorList.get(terrainMonitorKey).isReadyToDraw())
                        terrainMonitorList.get(terrainMonitorKey).getTerrain().updateCurrentLOD(/*player.getPosition()*/new Vektor3f(0,0,0));
                    renderer.putTerrain(terrainMonitorList.get(terrainMonitorKey).getTerrain());
                }
            }

        } catch(Exception e)
        {
            System.out.println("Generating new terrain fail");
        }

    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        renderer.clearTerrainInstances();
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig)
    {

        MainActivity.assets=getAssets();

        MeshTexture shrekTex = new MeshTexture(LoadMesh.loadTexture("shrek/sample_pic.png"));
        Mesh shrek= OBJLoader.loadObj("shrek.obj");
        TexMesh texturedShrek = new TexMesh(shrek,shrekTex);
        MeshInstance shrekIns=new MeshInstance(texturedShrek,new Vektor3f(-60,0,-60),0,0,0,1);
        this.shrekIns=shrekIns;

        Log.i("debug", "onSurfaceCreated");
        meshShader= new MeshShader(getIS(R.raw.vsmesh),getIS(R.raw.fsmesh));
        terrainShader = new TerrainShader(getIS(R.raw.vsterrain),getIS(R.raw.fsterrain));
        framebufferShader = new FramebufferShader(getIS(R.raw.vsframebuffer),getIS(R.raw.fsframebuffer));
        renderer=new Renderer(camera,meshShader,terrainShader,framebufferShader);



        TerrainTexture grass= new TerrainTexture(LoadMesh.loadTexture("grass.png"));
        TerrainTexture rock= new TerrainTexture(LoadMesh.loadTexture("sample_pic.png"));
        TerrainTexture snow= new TerrainTexture(LoadMesh.loadTexture("snow.png"));
        terrainMultiTexture=new TerrainMultiTexture(grass,rock, snow);




    }

    @Override
    public void onRendererShutdown() {

    }

}
