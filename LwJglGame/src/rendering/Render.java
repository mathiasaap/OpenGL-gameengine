package rendering;

import matrix.Matrix;
import matrix.Camera;
import mesh.Mesh;
import mesh.MeshInstance;
import mesh.TexMesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.MeshShader;

public class Render {
	
	public Render(MeshShader meshShader)
	{
		Matrix.uploadProjectionMatrix(meshShader);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glCullFace(GL11.GL_BACK);
		
		
	}
	
	public void prepareScene()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//GL11.glClearColor(0.2f, 0.3f, 0.6f, 1.0f);
		GL11.glClearColor((80f/256f), (16f/256f), (0f/256f), 1.0f);
	}
	public void draw(MeshInstance insMesh, MeshShader meshShader)
	{
		meshShader.uploadSpecular(insMesh.getMesh().getTex().getShine(),insMesh.getMesh().getTex().getReflectivity());
		GL30.glBindVertexArray(insMesh.getMesh().getMesh().getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		Matrix4f transformation = Matrix.transformationMatrix(insMesh.getPosition(), insMesh.getRotX(), insMesh.getRotY(), insMesh.getRotZ(), insMesh.getScale());
		meshShader.loadTranformationMatrix(transformation);
	
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,insMesh.getMesh().getTex().getTexId());
		GL11.glDrawElements(GL11.GL_TRIANGLES,insMesh.getMesh().getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
	}

}