package obj;

import java.util.Random;

import Model.VboBilboard;

public class InimigoVoador extends ObjtCene {

    private int vidaMaxima = 4; // Vida máxima do inimigo voador
    private int vidaAtual = vidaMaxima; // Vida atual do inimigo
    private static final Random rand = new Random();
    private float velocidade = 0.02f + rand.nextFloat() * 0.05f; // Velocidade de movimento
    private float direcaoX = rand.nextFloat() * (float) (2 * Math.PI);
    private float direcaoZ = rand.nextFloat() * (float) (2 * Math.PI);
    private float alturaVoo = 5.0f + rand.nextFloat() * 3.0f; // Altura fixa de voo
    private long tempoTrocaDirecao = 1000 + rand.nextInt(2000); // ms
    private long tempoAtual = 0;
    private long ultimoTiro = 0; // Timestamp do último tiro
    private long intervaloTiro = 3000; // Intervalo entre tiros

    public InimigoVoador(float x, float y, float z, float raio) {
        super(x, y, z, raio);
        this.raio = raio * 1.5f;
        this.y = alturaVoo; // Define a altura inicial do voo
    }

    @Override
public void SimulaSe(long diftime) {
    if (!vivo) {
        return; // Não atualiza se o inimigo estiver morto
    }

    super.SimulaSe(diftime);

    // Atualiza a posição com base na direção
    float novoX = this.x + (float) Math.cos(direcaoX) * velocidade * diftime / 16.0f;
    float novoZ = this.z + (float) Math.sin(direcaoZ) * velocidade * diftime / 16.0f;

    // Evita colisões com outros inimigos voadores
    for (Object3D obj : dados.Constantes.listaObjetos) {
        if (obj instanceof InimigoVoador && obj != this && obj.vivo) {
            float distancia = (float) Math.sqrt(
                    Math.pow(obj.x - novoX, 2)
                    + Math.pow(obj.y - this.y, 2)
                    + Math.pow(obj.z - novoZ, 2)
            );
            if (distancia < (this.raio + obj.raio)) {
                direcaoX = rand.nextFloat() * (float) (2 * Math.PI);
                direcaoZ = rand.nextFloat() * (float) (2 * Math.PI);
                return; // Evita colisão
            }
        }
    }

    // Atualiza posição
    this.x = novoX;
    this.z = novoZ;

    // Atualiza direção após o tempo de troca
    tempoAtual += diftime;
    if (tempoAtual > tempoTrocaDirecao) {
        direcaoX = rand.nextFloat() * (float) (2 * Math.PI);
        direcaoZ = rand.nextFloat() * (float) (2 * Math.PI);
        tempoTrocaDirecao = 1000 + rand.nextInt(2000);
        tempoAtual = 0;
    }
}

    public void resetarCooldown() {
        ultimoTiro = System.currentTimeMillis();
    }

    public void dispararProjetil(float playerX, float playerY, float playerZ, VboBilboard vboBilbord) {
    float velocidadeProjetil = 5.0f; // Velocidade do projétil
    
    
    // Calcula a direção do projétil
    float dx = playerX - this.x;
    float dy = playerY - this.y;
    float dz = playerZ - this.z;
    float distancia = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

    // Normaliza a direção
    dx /= distancia;
    dy /= distancia;
    dz /= distancia;

    // Cria o projétil
    Projetil proj = new Projetil(this.x, this.y, this.z);
    proj.vx = dx * velocidadeProjetil;
    proj.vy = dy * velocidadeProjetil;
    proj.vz = dz * velocidadeProjetil;
    proj.raio = 0.2f;
    proj.model = vboBilbord;

    // Adiciona o projétil à lista de objetos
    dados.Constantes.listaObjetos.add(proj);

    // Reseta o cooldown do inimigo
    resetarCooldown();
}

    public boolean podeAtirar() {
        long agora = System.currentTimeMillis();
        return (agora - ultimoTiro) >= intervaloTiro;
    }

    public void aplicarDano(int dano) {
        vidaAtual -= dano; // Reduz a vida do inimigo
        if (vidaAtual <= 0) {
            vivo = false; // Marca o inimigo como morto
        }
    }
}
