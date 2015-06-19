package rendering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.lwjgl.util.vector.Vector4f;

import shaders.MeshShader;

public class RenderMesh {
	private MeshShader shader;
	private Map<TexMesh, List<MeshInstance>> meshInstances = new HashMap<>();
	
	public RenderMesh(MeshShader meshShader,Map<TexMesh, List<MeshInstance>> meshInstances)
	{
		shader=meshShader;
		this.meshInstances=meshInstances;
		Matrix.uploadProjectionMatrix(meshShader);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		
	}
	public void draw(Vector4f plane)
	{
		shader.useProgram();
		shader.uploadClipPlane(plane);
		draw();
	}
	
	public void draw()
	{
		shader.useProgram();
		for(TexMesh mesh:meshInstances.keySet())
		{
			prepareMesh(mesh);
			List<MeshInstance> group=meshInstances.get(mesh);
			for(MeshInstance meshInstance:group)
			{
				prepareIns(meshInstance);
				GL11.glDrawElements(GL11.GL_TRIANGLES,meshInstance.getMesh().getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);
			}
			unbindMesh();
			
		}
		
		
	}
	private void prepareMesh(TexMesh mesh)
	{
		
		GL30.glBindVertexArray(mesh.getMesh().getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		shader.uploadSpecular(mesh.getTex().getShine(),mesh.getTex().getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,mesh.getTex().getTexId());
		
	}
	private void prepareIns(MeshInstance ins)
	{
		Matrix4f transformation = Matrix.transformationMatrix(ins.getPosition(), ins.getRotX(), ins.getRotY(), ins.getRotZ(), ins.getScale());
		shader.loadTranformationMatrix(transformation);	
	}
	private void unbindMesh()
	{
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
	}
	

}
