package mathiasap.cardboard.rendering;


import android.opengl.GLES30;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mathiasap.cardboard.R;
import mathiasap.cardboard.matrix.Camera;
import mathiasap.cardboard.matrix.Matrices;
import mathiasap.cardboard.mesh.MeshInstance;
import mathiasap.cardboard.mesh.TexMesh;
import mathiasap.cardboard.shaders.AbstractShader;
import mathiasap.cardboard.shaders.FramebufferShader;
import mathiasap.cardboard.shaders.MeshShader;
import mathiasap.cardboard.shaders.TerrainShader;
import mathiasap.cardboard.terrain.Terrain;
import mathiasap.cardboard.textures.DepthTexture;
import mathiasap.cardboard.textures.FBOTexture;
import mathiasap.cardboard.textures.RenderTexture;

public class Renderer
{
    private Camera camera;
    private AbstractShader meshShader,terrainShader;
    private FramebufferShader framebufferShader;
    private RenderMesh renderMesh;
    private RenderTerrain renderTerrain;
    private RenderFramebuffer renderFramebuffer;
    Map<TexMesh, List<MeshInstance>> meshInstances= new HashMap<>();;
    private List<Terrain> terrains = new ArrayList<>();
    private int FBO=-1;

    private FBOTexture depthBufferTexture, renderTexture;

    public Renderer(Camera camera,MeshShader meshShader,TerrainShader terrainShader,FramebufferShader framebufferShader)
    {
        GLES30.glClearColor(135f/256f,206f/256f,250f/256f,1);
        this.camera=camera;
        this.meshShader=meshShader;
        this.terrainShader=terrainShader;
        this.framebufferShader= framebufferShader;
        terrainShader.bindTexId();

        renderMesh=new RenderMesh(meshShader,meshInstances);
        renderTerrain= new RenderTerrain(terrainShader,terrains);
        renderFramebuffer = new RenderFramebuffer();

        int[] FBOs= new int[1];
        GLES30.glGenFramebuffers(1,FBOs,0);
        FBO=FBOs[0];


    }

    private boolean framebufferCreated=false;
    private void createFramebuffer(int width, int height)
    {
        renderTexture=new RenderTexture(width,height);
        depthBufferTexture= new DepthTexture(width,height);


        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, FBO);
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, renderTexture.getId(), 0);
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_DEPTH_ATTACHMENT, GLES30.GL_TEXTURE_2D, depthBufferTexture.getId(), 0);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        framebufferCreated=true;
    }

    public void render(/*int width, int height*/) {

       /* if(!framebufferCreated)
        {
            createFramebuffer(width,height);
        }*/


        GLES30.glClearColor(135f / 256f, 206f / 256f, 250f / 256f, 1);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        //GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);



       /* GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, FBO);
        GLES20.glClearColor(135f / 256f, 206f / 256f, 250f / 256f, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);*/

        terrainShader.useProgram();
        terrainShader.loadProjectionMatrix(camera.getPerspectiveMatrix());
        terrainShader.loadViewMatrix(camera.getViewMatrix());
        renderTerrain.draw();

        meshShader.useProgram();
        meshShader.loadProjectionMatrix(camera.getPerspectiveMatrix());
        meshShader.loadViewMatrix(camera.getViewMatrix());
        renderMesh.draw();

       /* GLES30.glDisable(GLES30.GL_DEPTH_TEST);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
        framebufferShader.useProgram();
        renderFramebuffer.draw(renderTexture);*/

    }


    public void putInstance(MeshInstance mInstance)
    {
        TexMesh model = mInstance.getMesh();

        if(!meshInstances.containsKey(model))
        {
            List<MeshInstance> tmpLstMIns = new ArrayList<>();
            tmpLstMIns.add(mInstance);
            meshInstances.put(model, tmpLstMIns);
        }
        else
        {
            meshInstances.get(model).add(mInstance);
        }
    }

    public void putTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void clearMeshInstances()
    {
        this.meshInstances.clear();
    }
    public void clearTerrainInstances()
    {
        this.terrains.clear();
    }


}
