package obj;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import shaders.ShaderProgram;

public class InimigoSolo extends ObjtCene {

    private int vidaMaxima = 4; // Vida máxima do inimigo
    private int vidaAtual = vidaMaxima; // Vida atual do inimigo    

    private static final Random rand = new Random();
    private float velocidade = 0.01f + rand.nextFloat() * 0.02f;
    private float direcao = rand.nextFloat() * (float) (2 * Math.PI);
    private long tempoTroca = 500 + rand.nextInt(1500); // ms
    private long tempoAtual = 0;
    private long ultimoTiro = 0; // Timestamp do último tiro
    private long intervaloTiro = 2000;

    public InimigoSolo(float x, float y, float z, float raio) {
        super(x, y, z, raio);
        this.raio = raio  * 1.5f;
    }

    @Override
    public void SimulaSe(long diftime) {
        float novoX = this.x + (float) Math.cos(direcao) * velocidade * diftime / 16.0f;
        float novoZ = this.z + (float) Math.sin(direcao) * velocidade * diftime / 16.0f;

        float tileSize = 0.04f * dados.Constantes.mapa.raio;
        int wh = dados.Constantes.mapa.model.wh;
        int ix = Math.round((novoX - dados.Constantes.mapa.x) / tileSize);
        int iz = Math.round((novoZ - dados.Constantes.mapa.z) / tileSize);

        // Verifica se o tanque está dentro dos limites do mapa
        if (ix >= 0 && iz >= 0 && ix < wh && iz < wh) {
            int altura = dados.Constantes.mapa.model.data[iz][ix];

            // Verifica se a altura corresponde ao mar (altura <= 0)
            if (altura > 0) {
                // Atualiza a posição do tanque
                this.x = novoX;
                this.z = novoZ;

                // Atualiza a altura do tanque com base no terreno
                float alturaReal = altura * 0.001f * dados.Constantes.mapa.raio + dados.Constantes.mapa.y;
                this.y = alturaReal + 0.01f;
            }
        }

        // Atualiza a direção do tanque após o tempo de troca
        tempoAtual += diftime;
        if (tempoAtual > tempoTroca) {
            direcao = rand.nextFloat() * (float) (2 * Math.PI);
            tempoTroca = 500 + rand.nextInt(1500);
            tempoAtual = 0;
        }
    }

    @Override
    public void DesenhaSe(ShaderProgram shader) {
        super.DesenhaSe(shader);

        // Renderiza a barra de vida acima do inimigo
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y + 0.6f, z); // Posiciona a barra acima do inimigo
        GL11.glColor3f(1.0f, 0.0f, 0.0f); // Vermelho para a barra de vida

        float larguraBarra = 0.4f;
        float alturaBarra = 0.05f;
        float proporcaoVida = (float) vidaAtual / vidaMaxima;

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(-larguraBarra / 2, 0, 0);
        GL11.glVertex3f(-larguraBarra / 2 + larguraBarra * proporcaoVida, 0, 0);
        GL11.glVertex3f(-larguraBarra / 2 + larguraBarra * proporcaoVida, alturaBarra, 0);
        GL11.glVertex3f(-larguraBarra / 2, alturaBarra, 0);
        GL11.glEnd();

        GL11.glPopMatrix();
    }

    public boolean podeAtirar() {
        long agora = System.currentTimeMillis();
        return (agora - ultimoTiro) >= intervaloTiro;
    }

    public void resetarCooldown() {
        ultimoTiro = System.currentTimeMillis();
    }

    public void aplicarDano(int dano) {
        vidaAtual -= dano;
        if (vidaAtual <= 0) {
            vivo = false; // O inimigo morre se a vida chegar a 0
        }
    }
}
