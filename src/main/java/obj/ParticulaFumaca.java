package obj;

import org.lwjgl.opengl.GL11;

import dados.Constantes;

public class ParticulaFumaca {
    private float x, y, z;
    private float tamanho;
    private float alpha;
    private float velocidadeExpansao;
    private float velocidadeFade;

    public ParticulaFumaca(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.tamanho = 1.5f;
        this.alpha = 1.0f;
        this.velocidadeExpansao = 0.2f;
        this.velocidadeFade = 0.1f;
    }

    public void render() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Constantes.texturaParticula); // Usa a textura de partículas

        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha); // Branco com transparência

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(x - tamanho, y - tamanho, z);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(x + tamanho, y - tamanho, z);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(x + tamanho, y + tamanho, z);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(x - tamanho, y + tamanho, z);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_BLEND);
    }

    public void update(long diftime) {
        tamanho += velocidadeExpansao * diftime / 1000.0f; // Expande o tamanho
        alpha -= velocidadeFade * diftime / 1000.0f; // Reduz a transparência
    }

    public boolean isAlive() {
        return alpha > 0; // A partícula está viva enquanto sua transparência for maior que 0
    }
}