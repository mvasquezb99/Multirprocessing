public class App {
    public static void main(String[] args) throws Exception {
        Initializer init = new Initializer(args);
        try {
            init.Initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
