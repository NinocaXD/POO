public class Banco {
    private Conta[] contas;
    private int contador;

    // Construtor
    public Banco() {
        contas = new Conta[10]; // Capacidade inicial de 10 contas
        contador = 0;
    }

    // Método para adicionar uma conta ao banco
    public void adicionarConta(Conta conta) {
        if (contador < contas.length) {
            contas[contador++] = conta; // Adiciona a conta e incrementa o contador
        } else {
            System.out.println("Capacidade máxima de contas atingida.");
        }
    }

    // Método para encontrar uma conta pelo número
    public Conta getConta(int numero) {
        for (int i = 0; i < contador; i++) {
            if (contas[i].getNumeroConta() == numero) {
                return contas[i]; // Retorna a conta encontrada
            }
        }
        return null; // Conta não encontrada
    }

    // Método para realizar um saque
    public boolean saque(int numeroConta, double valor) {
        Conta conta = getConta(numeroConta);
        if (conta != null) {
            return conta.saque(valor);
        }
        System.out.println("Conta não encontrada.");
        return false; // Conta não encontrada
    }

    // Método para realizar um depósito
    public void deposito(int numeroConta, double valor) {
        Conta conta = getConta(numeroConta);
        if (conta != null) {
            conta.deposito(valor);
        } else {
            System.out.println("Conta não encontrada.");
        }
    }

    // Método para transferir valores entre contas
    public void transferir(int numeroContaOrigem, int numeroContaDestino, double valor) {
        Conta contaOrigem = getConta(numeroContaOrigem);
        Conta contaDestino = getConta(numeroContaDestino);
        if (contaOrigem != null && contaDestino != null) {
            contaOrigem.transferePara(contaDestino, valor);
        } else {
            System.out.println("Uma ou ambas as contas não foram encontradas.");
        }
    }
}

public abstract class Conta {
    private Double saldo;
    private int numeroConta;
    private String nomeCliente;

    // Construtor
    public Conta(double saldo, int numeroConta, String nomeCliente) {
        if (numeroConta <= 0) {
            this.numeroConta = 1;
        } else {
            this.numeroConta = numeroConta;
        }
        this.saldo = saldo;
        this.nomeCliente = nomeCliente;
    }

    // Métodos
    public void deposito(double valor) {
        this.saldo = this.saldo + valor;
    }

    public boolean saque(Double valor) {
        if (valor <= saldo) {
           this.saldo = this.saldo - valor;
            return true;
        }
        return false;
    }

    public void transferePara(Conta contaDestino, double valor) {
        if (this.saque(valor)) {
            contaDestino.deposito(valor);
        } 
    }

    public double getSaldo() {
        return saldo;
    }

    public int getNumeroConta() {
        return numeroConta;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }
}

public class ContaComum extends Conta {
    // Construtor
    public ContaComum(double saldo, int numeroConta, String nomeCliente) {
        super(saldo, numeroConta, nomeCliente);
    }

    // Métodos
    @Override
    public boolean saque(double valor) {
        if (this.getSaldo() - valor >= 0) {
            super.saque(valor);
            return true;
        }
        return false;
    }
}

public class ContaEspecial extends Conta {
    // Atributos
    private Double limite;

    // Construtor
    public ContaEspecial(Double saldo, Double limite, int numeroConta, String nomeCliente) {
        super(saldo, numeroConta, nomeCliente);
        this.limite = limite;
    }

    // Métodos
    @Override
    public boolean saque(Double valor) {
        if (this.getSaldo() + this.limite - valor >= 0) {
            super.saque(valor);
            return true;
        }
        return false;
    }

    public void atualizaLimite(double novoLimite) {
        if (novoLimite >= 0) {
            this.limite = novoLimite;
        }
    }

    public double getLimite() {
        return limite;
    }
}

public class Principal {
    public static void main(String[] args) {
        Banco banco = new Banco();

        // Criando contas comuns e especiais
        Conta conta1 = new ContaComum(100.0, 1, "Cliente 1");
        ContaComum conta2 = new ContaComum(100.0, 2, "Cliente 2");
        ContaComum conta3 = new ContaComum(100.0, 3, "Cliente 3");
        Conta contaE1 = new ContaEspecial(50.0, 100.0, 4, "Cliente E1");
        ContaEspecial contaE2 = new ContaEspecial(30.0, 100.0, 5, "Cliente E2");

        // adiciona contas ao banco
        banco.adicionarConta(conta1);
        banco.adicionarConta(conta2);
        banco.adicionarConta(conta3);
        banco.adicionarConta(contaE1);
        banco.adicionarConta(contaE2);

        conta1.transferePara(contaE1, 150.0);
        contaE2.atualizaLimite(-100.0);
        contaE2.saque(10.0);

        banco.deposito(1, 100.0);
        banco.deposito(2, 200.0);

        if (!conta2.saque(2, 70.0)) {
            System.out.println("Saldo insuficiente!");
        }
        if (!conta1.saque(1, 150.0)) {
            System.out.println("Saldo insuficiente!");
        }

        conta3.transferePara(conta1, 130.0);

        // Exibindo saldos
        System.out.println("Saldo conta 1: " + conta1.getSaldo());
        System.out.println("Saldo conta E1: " + contaE1.getSaldo());
        System.out.println("Saldo conta 2: " + conta2.getSaldo());
        
    }
}
