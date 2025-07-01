package obj;

import org.lwjgl.opengl.GL11;

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
        this.tamanho = 0.1f;
        this.alpha = 1.0f;
        this.velocidadeExpansao = 0.02f;
        this.velocidadeFade = 0.01f;
    }

    public void render() {
        GL11.glColor4f(0.5f, 0.5f, 0.5f, alpha); // Cinza com transparência
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(x - tamanho, y - tamanho, z);
        GL11.glVertex3f(x + tamanho, y - tamanho, z);
        GL11.glVertex3f(x + tamanho, y + tamanho, z);
        GL11.glVertex3f(x - tamanho, y + tamanho, z);
        GL11.glEnd();
    }

    public void update(long diftime) {
        tamanho += velocidadeExpansao * diftime / 1000.0f;
        alpha -= velocidadeFade * diftime / 1000.0f;
        if (alpha <= 0) {
            alpha = 0;
        }
    }

    public boolean isAlive() {
        return alpha > 0;
    }
}