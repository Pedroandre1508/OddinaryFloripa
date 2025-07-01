package obj;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Model.Model;
import dados.Constantes;
import shaders.ShaderProgram;
import util.Utils3D;

public class MisselTeleguiado extends Object3D {
    public Vector3f cor = new Vector3f();
    public Model model = null;

    FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);
    public float rotvel = 0;

    public Vector4f Front = new Vector4f(0.0f, 0.0f, -1.0f, 1.0f);
    public Vector4f UP = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
    public Vector4f Right = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);

    private Object3D alvo; // Inimigo mais próximo
    private long tempoVida = 0;
    private boolean explodindo = false;
    private long tempoExplosao = 0;

    public MisselTeleguiado(float x, float y, float z, Object3D alvo) {
        super(x, y, z);
        this.alvo = alvo;
        this.raio = 0.2f; // Tamanho inicial do míssil
    }

    @Override
    public void DesenhaSe(ShaderProgram shader) {
        Matrix4f modelm1 = new Matrix4f();
        modelm1.translate(new Vector3f(x, y, z));
        Matrix4f modelm = Utils3D.positionMatrix(Front, UP, Right);
        Matrix4f.mul(modelm1, modelm, modelm);
        modelm.scale(new Vector3f(raio, raio, raio));

        int modellocation = glGetUniformLocation(shader.programID, "model");
        modelm.storeTranspose(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(modellocation, false, matrixBuffer);

        int bilbloc = glGetUniformLocation(shader.programID, "ligaluz");
        glUniform1i(bilbloc, 0);

        if (explodindo) {
            glBindTexture(GL_TEXTURE_2D, Constantes.texturaExplosao);
        } else {
            glBindTexture(GL_TEXTURE_2D, Constantes.texturaTiro);
        }

        model.draw();
        glUniform1i(bilbloc, 0);
    }

    @Override
    public void SimulaSe(long diftime) {
        super.SimulaSe(diftime);

        tempoVida += diftime;

        if (explodindo) {
            tempoExplosao += diftime;
            raio *= (diftime / 400.0f) + 1;
            if (tempoExplosao > 1000) {
                vivo = false;
            }
            return;
        }

        if (alvo != null && alvo.vivo) {
            // Lógica de perseguição ao inimigo mais próximo
            Vector3f direcao = new Vector3f(
                alvo.x - x,
                alvo.y - y,
                alvo.z - z
            );
            direcao.normalise();
            vx = direcao.x * 5.0f; // Velocidade do míssil
            vy = direcao.y * 5.0f;
            vz = direcao.z * 5.0f;

            Front = new Vector4f(direcao.x, direcao.y, direcao.z, 1.0f);
            Utils3D.vec3dNormilize(Front);
        }

        x += vx * diftime / 1000.0f;
        y += vy * diftime / 1000.0f;
        z += vz * diftime / 1000.0f;

        if (Constantes.mapa.testaColisao(x, y, z, raio) || (alvo != null && testaColisaoComAlvo())) {
            explodindo = true;
            vx = 0;
            vy = 0;
            vz = 0;
        }

        if (tempoVida > 5000) { // Tempo máximo de vida do míssil
            vivo = false;
        }
    }

    private boolean testaColisaoComAlvo() {
        float distancia = (float) Math.sqrt(
            Math.pow(alvo.x - x, 2) +
            Math.pow(alvo.y - y, 2) +
            Math.pow(alvo.z - z, 2)
        );
        return distancia < (raio + alvo.raio);
    }
}