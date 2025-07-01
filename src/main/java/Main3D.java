
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Model.VboBilboard;
import Model.VboCube;
import dados.Constantes;
import obj.HealthBar;
import obj.Mapa3D;
import obj.MisselTeleguiado;
import obj.ObjHTGsrtm;
import obj.ObjModel;
import obj.Object3D;
import obj.ObjtCene;
import obj.Projetil;
import shaders.StaticShader;
import util.TextureLoader;
import util.Utils3D;

public class Main3D {

    // Variáveis para controle da janela e câmera
    private long window;
    float viewAngX = 0;
    float viewAngY = 0;
    float scale = 1.0f;
    private int windowWidth;
    private int windowHeight;

    public Random rnd = new Random();

    // Objetos 3D e shaders
    VboCube vboc;
    VboBilboard vboBilbord;
    StaticShader shader;
    ArrayList<Object3D> listaObjetos = new ArrayList<>();

    // Vetores para posição e orientação da câmera
    Vector4f cameraPos = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
    Vector4f cameraVectorFront = new Vector4f(0.0f, 0.0f, 1.0f, 1.0f);
    Vector4f cameraVectorUP = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
    Vector4f cameraVectorRight = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);

    // Matriz de visão
    Matrix4f view = new Matrix4f();

    // Texturas e modelos
    int tgato;
    int tf104;
    int tMisselOBJ;
    ObjModel MisselOBJ;

    // Objeto do jogador
    ObjtCene player;
    HealthBar healthBar; // Barra de vida do jogador
    boolean GameOver = false; // Indica se o jogo está pausado devido à morte do jogador
    int gameOverTexture;

    // Variáveis para controle de lógica
    double angluz = 0;
    float angle = 0;

    // Buffer para matrizes
    FloatBuffer matrixBuffer = MemoryUtil.memAllocFloat(16);

    // Flags para controle de movimento
    boolean UP = false;
    boolean DOWN = false;
    boolean LEFT = false;
    boolean RIGHT = false;
    boolean FORWARD = false;
    boolean BACKWARD = false;
    boolean QBu = false;
    boolean EBu = false;
    boolean FIRE = false;

    // Método principal que inicia o jogo
    public void run() {
        // Inicializa o jogo e entra no loop principal
        init();
        loop();

        // Libera recursos ao fechar
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    // Inicializa a janela e configurações do jogo
    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        // Inicializa a barra de vida com 5 vidas
        healthBar = new HealthBar();
        // Inicializa o contexto NanoVG
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configurações da janela
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Criação da janela
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        windowWidth = vidmode.width(); // Armazena a largura da janela
        windowHeight = vidmode.height(); // Armazena a altura da janela
        window = glfwCreateWindow(windowWidth, windowHeight, "Tela cheia", glfwGetPrimaryMonitor(), NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (GameOver) {

                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true);
                }
                return; // Bloqueia todas as outras ações
            }
            if (action == GLFW_PRESS) {
                if (key == GLFW_KEY_W) {
                    UP = true;
                }
                if (key == GLFW_KEY_S) {
                    DOWN = true;
                }
                if (key == GLFW_KEY_D) {
                    RIGHT = true;
                }
                if (key == GLFW_KEY_A) {
                    LEFT = true;
                }
                if (key == GLFW_KEY_E) {
                    QBu = true;
                }
                if (key == GLFW_KEY_Q) {
                    EBu = true;
                }
                if (key == GLFW_KEY_UP) {
                    FORWARD = true;
                }
                if (key == GLFW_KEY_DOWN) {
                    BACKWARD = true;
                }
                if (key == GLFW_KEY_SPACE) {
                    FIRE = true;
                }
            }
            if (action == GLFW_RELEASE) {
                if (key == GLFW_KEY_W) {
                    UP = false;
                }
                if (key == GLFW_KEY_S) {
                    DOWN = false;
                }
                if (key == GLFW_KEY_D) {
                    RIGHT = false;
                }
                if (key == GLFW_KEY_A) {
                    LEFT = false;
                }
                if (key == GLFW_KEY_E) {
                    QBu = false;
                }
                if (key == GLFW_KEY_Q) {
                    EBu = false;
                }
                if (key == GLFW_KEY_UP) {
                    FORWARD = false;
                }
                if (key == GLFW_KEY_DOWN) {
                    BACKWARD = false;
                }
                if (key == GLFW_KEY_SPACE) {
                    FIRE = false;
                }
            }
            if (key == GLFW_KEY_Z) {
                scale = scale * 1.1f;
            }
            if (key == GLFW_KEY_X) {
                scale = scale * 0.9f;
            }
            if (key == GLFW_KEY_M && action == GLFW_PRESS) {
        Object3D inimigoMaisProximo = encontrarInimigoMaisProximo();
        if (inimigoMaisProximo != null) {
            MisselTeleguiado missel = new MisselTeleguiado(player.x, player.y, player.z, inimigoMaisProximo);
            missel.model = vboc; // Modelo do míssil
            listaObjetos.add(missel);
        }
        }

        });

        // try {
        //     AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("freeze.wav"));
        //     Clip clip = AudioSystem.getClip();
        //     clip.open(audioStream);

        //     // Ajuste de volume
        //     FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        //     float volume = (float) (Math.log(0.5) / Math.log(10) * 20); // 50% do volume máximo
        //     volumeControl.setValue(volume);

        //     clip.start();
        // } catch (Exception e) {
        //     System.err.println("error load music");
        // }

        // Configura contexto OpenGL	
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private Object3D encontrarInimigoMaisProximo() {
        Object3D inimigoMaisProximo = null;
        float menorDistancia = Float.MAX_VALUE;
    
        for (Object3D obj : listaObjetos) {
            if (obj instanceof ObjtCene && obj != player && obj.vivo) {
                float distancia = (float) Math.sqrt(
                    Math.pow(obj.x - player.x, 2) +
                    Math.pow(obj.y - player.y, 2) +
                    Math.pow(obj.z - player.z, 2)
                );
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    inimigoMaisProximo = obj;
                }
            }
        }
        return inimigoMaisProximo;
    }

    // Loop principal do jogo
    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Carrega texturas e modelos
        BufferedImage imggato = TextureLoader.loadImage("textures/texturaGato.jpeg");
        BufferedImage imgf104 = TextureLoader.loadImage("textures/skz.jpeg"); // textura jato
        BufferedImage imgMisselOBJ = TextureLoader.loadImage("textures/bomba.jpg");

        BufferedImage gatorgba = new BufferedImage(imggato.getWidth(), imggato.getHeight(), BufferedImage.TYPE_INT_ARGB);
        gatorgba.getGraphics().drawImage(imggato, 0, 0, null);
        tgato = TextureLoader.loadTexture(imggato);
        tf104 = TextureLoader.loadTexture(imgf104);
        tMisselOBJ = TextureLoader.loadTexture(imgMisselOBJ);

        Constantes.tgato = tgato;

        BufferedImage imgmulttexture = TextureLoader.loadImage("textures/multtexture.png");
        Constantes.tmult = TextureLoader.loadTexture(imgmulttexture);

        BufferedImage texturamig = TextureLoader.loadImage("textures/TexturaMig01.png");
        Constantes.txtmig = TextureLoader.loadTexture(texturamig);

        BufferedImage imgtexttiro = TextureLoader.loadImage("textures/texturaTiro.png");
        Constantes.texturaTiro = TextureLoader.loadTexture(imgtexttiro);

        BufferedImage imgtextexp = TextureLoader.loadImage("textures/texturaExplosao.png");
        Constantes.texturaExplosao = TextureLoader.loadTexture(imgtextexp);

        vboc = new VboCube();
        vboc.load();
        vboBilbord = new VboBilboard();
        vboBilbord.load();
        shader = new StaticShader();

        //Configuração do Player
        ObjModel f101 = new ObjModel();
        f101.loadObj("models/f104starfighter.obj");
        f101.load();

        player = new ObjtCene(0, 0, 0, 0.1f);
        player.model = f101;
        player.vz = 0.0f;
        player.texture = tf104;

        Matrix4f f104ajust = new Matrix4f();
        f104ajust.setIdentity();
        f104ajust.rotate(3.14f, new Vector3f(1, 0, 0));
        f104ajust.rotate(3.14f, new Vector3f(0, 0, 1));
        player.modelAjust = f104ajust;

        listaObjetos.add(player);

        MisselOBJ = new ObjModel();
        MisselOBJ.loadObj("models/tank.obj"); // Certifique-se de que o caminho está correto
        MisselOBJ.load();

        for (int i = 0; i < 1000; i++) {
            ObjtCene cubo = new ObjtCene(rnd.nextFloat() * 10 - 5, rnd.nextFloat() * 10 - 5, rnd.nextFloat() * 10 - 5, rnd.nextFloat() * 0.05f + 0.02f);
            cubo.model = vboc;
            cubo.vx = 0;
            cubo.vy = 0;
            cubo.vz = 0;
            cubo.rotvel = rnd.nextFloat() * 9;
            cubo.texture = tgato;
            listaObjetos.add(cubo);
        }

        ObjHTGsrtm mapmodel = new ObjHTGsrtm();
        mapmodel.load();

        Constantes.mapa = new Mapa3D(-10.0f, -10.0f, -10.0f, 10);
        Constantes.mapa.model = mapmodel;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        // Loop de renderização e atualização
        int frame = 0;
        long lasttime = System.currentTimeMillis();

        long ultimoTempo = System.currentTimeMillis();
        while (!glfwWindowShouldClose(window)) {

            long diftime = System.currentTimeMillis() - ultimoTempo;
            ultimoTempo = System.currentTimeMillis();

            gameUptade(diftime);
            gameRender();

            frame++;
            long actualTime = System.currentTimeMillis();
            if ((lasttime / 1000) != (actualTime / 1000)) {
                frame = 0;
                lasttime = actualTime;
            }

        }
    }

    long tirotimer = 0;

    public void gameUptade(long diftime) {
        float vel = 5.0f;

        tirotimer += diftime;

        angluz = 0;

        Matrix4f rotTmp = new Matrix4f();
        rotTmp.setIdentity();
        if (RIGHT) {
            rotTmp.rotate(-1.0f * diftime / 1000.0f, new Vector3f(cameraVectorUP.x, cameraVectorUP.y, cameraVectorUP.z));
        }
        if (LEFT) {
            rotTmp.rotate(1.0f * diftime / 1000.0f, new Vector3f(cameraVectorUP.x, cameraVectorUP.y, cameraVectorUP.z));
        }
        if (UP) {
            rotTmp.rotate(-1.0f * diftime / 1000.0f, new Vector3f(cameraVectorRight.x, cameraVectorRight.y, cameraVectorRight.z));
        }
        if (DOWN) {
            rotTmp.rotate(1.0f * diftime / 1000.0f, new Vector3f(cameraVectorRight.x, cameraVectorRight.y, cameraVectorRight.z));
        }
        if (QBu) {
            rotTmp.rotate(-1.0f * diftime / 1000.0f, new Vector3f(cameraVectorFront.x, cameraVectorFront.y, cameraVectorFront.z));
        }
        if (EBu) {
            rotTmp.rotate(1.0f * diftime / 1000.0f, new Vector3f(cameraVectorFront.x, cameraVectorFront.y, cameraVectorFront.z));
        }

        rotTmp.transform(rotTmp, cameraVectorFront, cameraVectorFront);
        rotTmp.transform(rotTmp, cameraVectorRight, cameraVectorRight);
        rotTmp.transform(rotTmp, cameraVectorUP, cameraVectorUP);

        Utils3D.vec3dNormilize(cameraVectorFront);
        Utils3D.vec3dNormilize(cameraVectorRight);
        Utils3D.vec3dNormilize(cameraVectorUP);

        if (FORWARD) {
            cameraPos.x -= cameraVectorFront.x * vel * diftime / 1000.0f;
            cameraPos.y -= cameraVectorFront.y * vel * diftime / 1000.0f;
            cameraPos.z -= cameraVectorFront.z * vel * diftime / 1000.0f;
        }
        if (BACKWARD) {
            cameraPos.x += cameraVectorFront.x * vel * diftime / 1000.0f;
            cameraPos.y += cameraVectorFront.y * vel * diftime / 1000.0f;
            cameraPos.z += cameraVectorFront.z * vel * diftime / 1000.0f;
        }

        Vector4f t = new Vector4f(cameraPos.dot(cameraPos, cameraVectorRight), cameraPos.dot(cameraPos, cameraVectorUP), cameraPos.dot(cameraPos, cameraVectorFront), 1.0f);

        view = Utils3D.setLookAtMatrix(t, cameraVectorFront, cameraVectorUP, cameraVectorRight);

        Matrix4f transf = new Matrix4f();
        transf.setIdentity();
        transf.translate(new Vector3f(1, 1, 0));
        view.mul(transf, view, view);

        // Suavização de rotação do player
        player.Front = Utils3D.slerp(player.Front, cameraVectorFront, 0.3f); // Interpolação suave
        Utils3D.vec3dNormilize(player.Front); // Normaliza o vetor após a interpolação

        player.UP = Utils3D.slerp(player.UP, cameraVectorUP, 0.3f);
        Utils3D.vec3dNormilize(player.UP); // Normaliza o vetor após a interpolação

        player.Right = Utils3D.slerp(player.Right, cameraVectorRight, 0.3f);
        Utils3D.vec3dNormilize(player.Right); // Normaliza o vetor após a interpolação

        // Verifica se os vetores interpolados são válidos
        if (Float.isNaN(player.Front.x) || Float.isNaN(player.Front.y) || Float.isNaN(player.Front.z) ||
            Float.isNaN(player.UP.x) || Float.isNaN(player.UP.y) || Float.isNaN(player.UP.z) ||
            Float.isNaN(player.Right.x) || Float.isNaN(player.Right.y) || Float.isNaN(player.Right.z)) {
            player.Front = cameraVectorFront; // Reajusta para valores padrão
            player.UP = cameraVectorUP;
            player.Right = cameraVectorRight;
        }

        // Atualiza a posição do player com base na sua orientação
        player.x = cameraPos.x + player.Front.x * - 0.3f - player.UP.x * 0.4f;
        player.y = cameraPos.y + player.Front.y * - 0.3f - player.UP.y * 0.4f;
        player.z = cameraPos.z + player.Front.z * - 0.3f - player.UP.z * 0.4f;

        if (GameOver) {
            // Limpa os estados das teclas para impedir movimento
            UP = false;
            DOWN = false;
            LEFT = false;
            RIGHT = false;
            FORWARD = false;
            BACKWARD = false;
            QBu = false;
            EBu = false;
            FIRE = false;
            return;
        }

        Constantes.mapa.testaColisao(player.x, player.y, player.z, 0.1f);
        // Exemplo de decremento de vida ao tomar dano
        if (Constantes.mapa.testaColisao(player.x, player.y, player.z, player.raio)) {
            healthBar.updateLives(healthBar.getCurrentLives() - 5); // Reduz 1 vida
        }

        if (healthBar.getCurrentLives() <= 0) {
            GameOver = true; // Marca o jogo como terminado
            return; // Impede que o restante da lógica seja executado
        }

        if (FIRE && tirotimer >= 100) {
            float velocidade_projetil = 14;
            Projetil pj = new Projetil(player.x + cameraVectorRight.x * 0.5f + cameraVectorUP.x * 0.2f,
                    player.y + cameraVectorRight.y * 0.5f + cameraVectorUP.y * 0.2f,
                    player.z + cameraVectorRight.z * 0.5f + cameraVectorUP.z * 0.2f);
            pj.vx = -cameraVectorFront.x * velocidade_projetil;
            pj.vy = -cameraVectorFront.y * velocidade_projetil;
            pj.vz = -cameraVectorFront.z * velocidade_projetil;
            pj.raio = 0.2f;
            pj.model = vboBilbord;
            pj.setRotation(cameraVectorFront, cameraVectorUP, cameraVectorRight);

            listaObjetos.add(pj);

            pj = new Projetil(player.x - cameraVectorRight.x * 0.5f + cameraVectorUP.x * 0.2f,
                    player.y - cameraVectorRight.y * 0.5f + cameraVectorUP.y * 0.2f,
                    player.z - cameraVectorRight.z * 0.5f + cameraVectorUP.z * 0.2f);
            pj.vx = -cameraVectorFront.x * velocidade_projetil;
            pj.vy = -cameraVectorFront.y * velocidade_projetil;
            pj.vz = -cameraVectorFront.z * velocidade_projetil;
            pj.raio = 0.2f;
            pj.model = vboBilbord;
            pj.setRotation(cameraVectorFront, cameraVectorUP, cameraVectorRight);

            listaObjetos.add(pj);
            tirotimer = 0;
        }

        for (int i = 0; i < listaObjetos.size(); i++) {
            Object3D obj = listaObjetos.get(i);
            obj.SimulaSe(diftime);
            if (obj.vivo == false) {
                listaObjetos.remove(i);
                i--;
            }
        }

        angle += 0.1;
    }

    // Renderiza os objetos na tela
    float ly = 0;
    float linc = 0.2f;

    public void gameRender() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        shader.start();

        int lightpos = glGetUniformLocation(shader.programID, "lightPosition");
        ly += linc;
        if (ly > 25) {
            linc = -0.2f;
        }
        if (ly < -25) {
            linc = 0.2f;
        }
        float vf[] = {63, 200, 63, 1.0f};
        glUniform4fv(lightpos, vf);

        // Luzes -------------------------------------------------
        int lpos = glGetUniformLocation(shader.programID, "lpos");
        int nlights = glGetUniformLocation(shader.programID, "nlights");
        int lconf = glGetUniformLocation(shader.programID, "lconf");

        float vl1[] = {10, ly, 10};
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vl1.length);
        buffer.put(vl1).flip();

        float vconf[] = {3.0f, 20, 0};
        FloatBuffer buffer2 = BufferUtils.createFloatBuffer(vconf.length);
        buffer2.put(vconf).flip();

        glUniform1i(nlights, 1);
        glUniform3fv(lpos, buffer);
        glUniform3fv(lconf, buffer2);

        int posligaluz = glGetUniformLocation(shader.programID, "ligaluz");

        glUniform1i(posligaluz, 1);

        int projectionlocation = glGetUniformLocation(shader.programID, "projection");
        Matrix4f projection = setFrustum(-0.11f, 0.11f, -0.11f, 0.11f, 0.1f, 1000.0f);

        projection.storeTranspose(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(projectionlocation, false, matrixBuffer);

        glEnable(GL_DEPTH_TEST);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, tgato);

        int loctexture = glGetUniformLocation(shader.programID, "tex");

        glUniform1i(loctexture, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, Constantes.tmult);
        glUniform1i(loctexture, 0);

        int viewlocation = glGetUniformLocation(shader.programID, "view");

        view.storeTranspose(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(viewlocation, false, matrixBuffer);

        Constantes.mapa.DesenhaSe(shader);

        for (int i = 0; i < listaObjetos.size(); i++) {
            listaObjetos.get(i).DesenhaSe(shader);
        }

        shader.stop();

        // Configura a projeção ortográfica para renderizar elementos 2D
        glMatrixMode(GL11.GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, windowWidth, windowHeight, 0, -1, 1); // Coordenadas 2D
        glMatrixMode(GL11.GL_MODELVIEW);
        glLoadIdentity();

        // Renderiza a barra de vida (vermelha)
        healthBar.render(windowWidth, windowHeight);

        // Renderiza "Game Over" se o jogador morrer
        if (GameOver) {
            loadGameOverTexture();
            renderGameOverTexture(500, 200);
        }
        glfwSwapBuffers(window); // Troca os buffers de cor
        glfwPollEvents();
    }

    private void loadGameOverTexture() {
        BufferedImage gameOverImage = TextureLoader.loadImage("textures/gameOver.png");
        gameOverTexture = TextureLoader.loadTexture(gameOverImage);
    }

    private void renderGameOverTexture(float width, float height) {
        glEnable(GL_BLEND); // Habilita o blending para transparência
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // Configura o blending

        glEnable(GL_TEXTURE_2D); // Habilita o uso de texturas
        glBindTexture(GL_TEXTURE_2D, gameOverTexture); // Vincula a textura do PNG

        glColor4f(1.0f, 0.0f, 1.0f, 6.0f); // RGBA: Branco com opacidade total

        // Configura a projeção ortográfica para renderizar no espaço 2D da janela
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, windowWidth, windowHeight, 0, -1, 1); // Coordenadas 2D da janela

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Renderiza a textura diretamente na interface da janela
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(windowWidth / 2 - width / 2, windowHeight / 2 - height / 2); // Inferior esquerda
        glTexCoord2f(1, 0);
        glVertex2f(windowWidth / 2 + width / 2, windowHeight / 2 - height / 2); // Inferior direita
        glTexCoord2f(1, 1);
        glVertex2f(windowWidth / 2 + width / 2, windowHeight / 2 + height / 2); // Superior direita
        glTexCoord2f(0, 1);
        glVertex2f(windowWidth / 2 - width / 2, windowHeight / 2 + height / 2); // Superior esquerda
        glEnd();

        glDisable(GL_TEXTURE_2D); // Desabilita o uso de texturas
        glDisable(GL_BLEND); // Desabilita o blending
    }

    public static void main(String[] args) {
        new Main3D().run();
    }

    // Métodos auxiliares para matrizes e vetores
    public static void gluPerspective(float fovy, float aspect, float near, float far) {
        float bottom = -near * (float) Math.tan(fovy / 2);
        float top = -bottom;
        float left = aspect * bottom;
        float right = -left;
        glFrustum(left, right, bottom, top, near, far);
    }

    public static Matrix4f setLookAtMatrix(Vector4f pos, Vector4f front, Vector4f up, Vector4f right) {
        Matrix4f m = new Matrix4f();
        m.m00 = right.x;
        m.m01 = up.x;
        m.m02 = front.x;
        m.m03 = 0.0f;

        m.m10 = right.y;
        m.m11 = up.y;
        m.m12 = front.y;
        m.m13 = 0.0f;

        m.m20 = right.z;
        m.m21 = up.z;
        m.m22 = front.z;
        m.m23 = 0.0f;

        m.m30 = -pos.x;
        m.m31 = -pos.y;
        m.m32 = -pos.z;
        m.m33 = 1.0f;

        return m;
    }

    public static Matrix4f setLookAtMatrixB(Vector4f pos, Vector4f front, Vector4f up, Vector4f right) {
        Matrix4f m = new Matrix4f();
        m.m00 = right.x;
        m.m01 = right.y;
        m.m02 = right.z;
        m.m03 = pos.x;

        m.m10 = up.x;
        m.m11 = up.y;
        m.m12 = up.z;
        m.m13 = pos.y;

        m.m20 = front.x;
        m.m21 = front.y;
        m.m22 = front.z;
        m.m23 = pos.z;

        m.m30 = 0.0f;
        m.m31 = 0.0f;
        m.m32 = 0.0f;
        m.m33 = 1.0f;

        return m;
    }

    public static double vecMag(Vector4f v) {
        return Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }

    public static void vec3dNormilize(Vector4f v) {
        double mag = vecMag(v);
        v.setX((float) (v.x / mag));
        v.setY((float) (v.y / mag));
        v.setZ((float) (v.z / mag));
    }

    Matrix4f setFrustum(float l, float r, float b, float t, float n, float f) {
        Matrix4f m = new Matrix4f();
        m.m00 = 2 * n / (r - l);
        m.m01 = 0.0f;
        m.m02 = 0.0f;
        m.m03 = 0.0f;

        m.m10 = 0.0f;
        m.m11 = 2 * n / (t - b);
        m.m12 = 0.0f;
        m.m13 = 0.0f;

        m.m20 = (r + l) / (r - l);
        m.m21 = (t + b) / (t - b);
        m.m22 = -(f + n) / (f - n);
        m.m23 = -1;

        m.m30 = 0.0f;
        m.m31 = 0.0f;
        m.m32 = -(2 * f * n) / (f - n);
        m.m33 = 0;

        return m;
    }

}