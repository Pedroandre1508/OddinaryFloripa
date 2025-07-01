package obj;

import java.util.Random;

public class InimigoSolo extends ObjtCene {
    private static final Random rand = new Random();
    private float velocidade = 0.03f + rand.nextFloat() * 0.04f;
    private float direcao = rand.nextFloat() * (float)(2 * Math.PI);
    private long tempoTroca = 500 + rand.nextInt(1500); // ms
    private long tempoAtual = 0;
    private long ultimoTiro = 0; // Timestamp do último tiro
    private long intervaloTiro = 2000;

    public InimigoSolo(float x, float y, float z, float raio) {
        super(x, y, z, raio);
        this.raio = raio;
    }

    @Override
    public void SimulaSe(long diftime) {
        this.x += Math.cos(direcao) * velocidade * diftime / 16.0;
        this.z += Math.sin(direcao) * velocidade * diftime / 16.0;

        float tileSize = 0.04f * dados.Constantes.mapa.raio;
        int wh = dados.Constantes.mapa.model.wh;
        int ix = Math.round((this.x - dados.Constantes.mapa.x) / tileSize);
        int iz = Math.round((this.z - dados.Constantes.mapa.z) / tileSize);

        if (ix >= 0 && iz >= 0 && ix < wh && iz < wh) {
            float altura = dados.Constantes.mapa.model.data[iz][ix] * 0.001f * dados.Constantes.mapa.raio + dados.Constantes.mapa.y;
            this.y = altura + 0.01f;
        }

        tempoAtual += diftime;
        if (tempoAtual > tempoTroca) {
            direcao = rand.nextFloat() * (float)(2 * Math.PI);
            tempoTroca = 500 + rand.nextInt(1500);
            tempoAtual = 0;
        }
    }

    public boolean podeAtirar() {
        long agora = System.currentTimeMillis();
        return (agora - ultimoTiro) >= intervaloTiro;
    }
    
    public void resetarCooldown() {
        ultimoTiro = System.currentTimeMillis();
    }
}