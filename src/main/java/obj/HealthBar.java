package obj;

import org.lwjgl.opengl.GL11;

/**
 * Classe responsável por gerenciar e renderizar a barra de vida do jogador.
 * A barra é dividida em 5 partes, e cada parte é removida ao tomar dano.
 */
public class HealthBar {
    private int maxLives = 5; // Número máximo de vidas
    private int currentLives = maxLives; // Vidas atuais

    /**
     * Atualiza as vidas atuais do jogador.
     * @param lives Nova quantidade de vidas.
     */
    public void updateLives(int lives) {
        this.currentLives = Math.max(0, Math.min(lives, maxLives)); // Garante que as vidas estejam entre 0 e maxLives
    }

    /**
     * Renderiza a barra de vida na tela.
     * @param windowWidth Largura da janela.
     * @param windowHeight Altura da janela.
     */
    public void render(int windowWidth, int windowHeight) {
        float barWidth = 200; // Largura total da barra
        float barHeight = 20; // Altura da barra
        float segmentWidth = barWidth / maxLives; // Largura de cada segmento
        float x = windowWidth - barWidth - 10; // Posição X na tela
        float y = 10; // Posição Y na tela (superior)

        // Renderiza os segmentos vermelhos correspondentes às vidas atuais
        GL11.glColor3f(1.0f, 0.0f, 0.0f); // Vermelho
        for (int i = 0; i < currentLives; i++) {
            float segmentX = x + (i * segmentWidth);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(segmentX, y);
            GL11.glVertex2f(segmentX + segmentWidth, y);
            GL11.glVertex2f(segmentX + segmentWidth, y + barHeight);
            GL11.glVertex2f(segmentX, y + barHeight);
            GL11.glEnd();
        }
    }

    public int getCurrentLives() {
        return currentLives;
    }
}