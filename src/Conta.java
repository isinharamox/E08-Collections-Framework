import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Conta implements ITaxas {

    private int numero;

    private Cliente dono;

    private double saldo;

    protected double limite;

    private ArrayList<Operacao> operacoes;

    private int proximaOperacao;

    private static int totalContas = 0;

    public Conta(int numero, Cliente dono, double saldo, double limite) {
        this.numero = numero;
        this.dono = dono;
        this.saldo = saldo;
        this.limite = limite;

        this.operacoes = new ArrayList<Operacao>();
        this.proximaOperacao = 0;

        Conta.totalContas++;
    }

    public boolean sacar(double valor) {
        if (valor >= 0 && valor <= this.limite) {
            this.saldo -= valor;

            this.operacoes.add(new OperacaoSaque(valor));
            this.proximaOperacao++;
            return true;
        }

        return false;
    }

    public void depositar(double valor) {
        this.saldo += valor;

        this.operacoes.add(new OperacaoDeposito(valor));
        this.proximaOperacao++;
    }

    public boolean transferir(Conta destino, double valor) {
        boolean valorSacado = this.sacar(valor);
        if (valorSacado) {
            destino.depositar(valor);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return dono.toString() + '\n' +
                "---\n" +
                "numero=" + numero + '\n' +
                "saldo=" + saldo + '\n' +
                "limite=" + limite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof Conta) {
            Conta conta = (Conta) o;
            return numero == conta.numero;
        }
        return false;
    }

    public void imprimirExtrato(int tipo) {

        if(tipo == 0) {
            System.out.println("======= Extrato Conta " + this.numero + "======");
            for(Operacao atual : this.operacoes) {
                if (atual != null) {
                    System.out.println(atual);
                }
            }
            System.out.println("====================");
        } else{
        ArrayList<Operacao> ordenada = new ArrayList<Operacao>();
        for (Operacao opr : this.operacoes) ordenada.add(opr);
        Collections.sort(ordenada);
        System.out.println("======= Extrato Contai " + this.numero + "======");
        for(Operacao atual : ordenada) {
            if (atual != null) {
                System.out.println(atual);
            }
        }
        System.out.println("====================");
    }
}

public void imprimirExtratoTaxas() {
    System.out.println("=== Extrato de Taxas ===");
    System.out.printf("Manutenção:\t%.2f\n", this.calcularTaxas());

    double totalTaxas = this.calcularTaxas();
    for (Operacao atual : this.operacoes) {

        totalTaxas += atual.calcularTaxas();
        System.out.printf("%c:\t%.2f\n", atual.getTipo(), atual.calcularTaxas());
    }

    System.out.printf("Total:\t%.2f\n", totalTaxas);
}

public int getNumero() {
    return numero;
}

public Cliente getDono() {
    return dono;
}

public double getSaldo() {
    return saldo;
}

public double getLimite() {
    return limite;
}

public static int getTotalContas() {
    return Conta.totalContas;
}

public void setNumero(int numero) {
    this.numero = numero;
}

public void setDono(Cliente dono) {
    this.dono = dono;
}

public abstract void setLimite(double limite);
}