package ui;

import org.lwjgl.opengl.GL11;

/**
 * Classe genérica para gerenciar e renderizar barras de vida.
 * Pode ser associada a qualquer objeto no jogo.
 */
public class HealthBar {
    private int maxHealth; // Vida máxima
    private int currentHealth; // Vida atual

    public HealthBar(int maxHealth, float width, float height, float offsetX, float offsetY) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    /**
     * Atualiza a vida atual.
     * @param health Nova vida.
     */
    public void updateHealth(int health) {
        this.currentHealth = Math.max(0, Math.min(health, maxHealth)); // Garante que a vida esteja entre 0 e maxHealth
    }

    /**
     * Renderiza a barra de vida na tela.
     * @param x Posição X na tela.
     * @param y Posição Y na tela.
     */
    public void render(int windowWidth, int windowHeight) {
        float barWidth = 200; // Largura da barra
        float barHeight = 20; // Altura da barra
        float x = windowWidth - barWidth - 10; // Posição X na tela
        float y = 10; // Posição Y na tela (superior)

        // Renderiza o fundo branco
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - 5, y - 5); // Expande o fundo um pouco além da barra
        GL11.glVertex2f(x + barWidth + 5, y - 5);
        GL11.glVertex2f(x + barWidth + 5, y + barHeight + 5);
        GL11.glVertex2f(x - 5, y + barHeight + 5);
        GL11.glEnd();

        // Renderiza o fundo da barra (cor preta)
        GL11.glColor3f(0.0f, 0.0f, 0.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + barWidth, y);
        GL11.glVertex2f(x + barWidth, y + barHeight);
        GL11.glVertex2f(x, y + barHeight);
        GL11.glEnd();

        // Renderiza a barra de vida (cor vermelha)
        float healthRatio = (float) currentHealth / maxHealth;
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + (barWidth * healthRatio), y);
        GL11.glVertex2f(x + (barWidth * healthRatio), y + barHeight);
        GL11.glVertex2f(x, y + barHeight);
        GL11.glEnd();
    }

    public int getCurrentHealth() {
        // TODO Auto-generated method stub
    return currentHealth;    }
}