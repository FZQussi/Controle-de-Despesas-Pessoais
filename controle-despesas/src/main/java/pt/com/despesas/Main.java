package pt.com.despesas;

import pt.com.despesas.service.DespesaService;

public class Main {

    public static void main(String[] args) {
        DespesaService service = new DespesaService();
        Menu menu = new Menu(service);
        menu.iniciar();
    }
}
