package com.uca;

import java.util.Scanner;

public class Main {

    private static final int MAX_INSTRUCTIONS = 200;
    private static final int STACK_SIZE = 500;
    private static int codeCounter = 0;

    private static PInstruction code[] = new PInstruction[MAX_INSTRUCTIONS];
    private static Data stack[] = new Data[STACK_SIZE];

    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Error: No se ha proporcionado el nombre del programa");
            return;
        }
        String fileName = args[0];
        read(fileName);
        run();
    }

    private static void read(String filename){
        ErrorLog.init();
        FileManager file = new FileManager(filename);
        file.openFile();
        codeCounter=0;
        while (!file.isEndOfFile()){
            PInstruction p = new PInstruction();
            String line = file.getNextLine();
            if (line.isEmpty()){
                continue;
            }
            int i = line.indexOf(' ');
            String pcodeString = line.substring(0,i);
            PCode pc = PCode.get(pcodeString);
            p.setPcode(pc);
            line = line.substring(i+1, line.length());

            i = line.indexOf(' ');
            String niString = line.substring(0, i);
            int ni = Integer.parseInt(niString);
            p.setNi(ni);
            line = line.substring(i+1, line.length());

            String di = line;
            if (pc == PCode.LIT){
                LIT newP = null;
                switch (ni){
                    case 0:
                        newP = new LIT<Integer>(Integer.parseInt(di));
                        break;
                    case 1:
                        newP = new LIT<Double>(Double.parseDouble(di));
                        break;
                    case 2:
                        newP = new LIT<Character>(di.charAt(0));
                        break;
                    case 3:
                        newP = new LIT<String>(di);
                        break;
                    case 4:
                        newP = new LIT<Boolean>(Boolean.parseBoolean(di));
                        break;
                }
                newP.setPcode(p.getPcode());
                newP.setNi(p.getNi());
                p = newP;
            }else{
                p.setDi(Integer.parseInt(di));
            }

            code[codeCounter] = p;
            codeCounter++;
        }
        file.closeFile();
        ErrorLog.close();

    }

    private static void run(){
        int ip=0;
        int sp=-1;
        int bp=0;
        PInstruction i;

        while (ip < codeCounter){
            i = code[ip];
            System.out.print("IP: "+(ip)+" ");
            ip++;
            switch (i.getPcode()){
                case LIT:
                    sp++;
                    switch (i.getNi()){
                        case 0:
                            stack[sp] = new Data<Integer>(((LIT<Integer>)i).getValue());
                            break;
                        case 1:
                            stack[sp] = new Data<Double>(((LIT<Double>)i).getValue());
                            break;
                        case 2:
                            stack[sp] = new Data<Character>(((LIT<Character>)i).getValue());
                            break;
                        case 3:
                            stack[sp] = new Data<String>(((LIT<String>)i).getValue());
                            break;
                        case 4:
                            stack[sp] = new Data<Boolean>(((LIT<Boolean>)i).getValue());
                            break;
                    }
                    System.out.println("LIT: Cargando el valor "+stack[sp].getValue().toString()+" en la direccion "+sp);
                    break;
                case OPR:
                    switch (i.getDi()){
                        case 0:
                            break;
                        case 1:
                            System.out.println("OPR: Output");
                            System.out.print(stack[sp].getValue().toString());
                            break;
                        case 2:
                            System.out.println("OPR: Input");
                            Scanner scanner = new Scanner(System.in);
                            sp++;
                            stack[sp] = new Data<Integer>(scanner.nextInt());
                            break;
                        case 3:
                            sp--;
                            int res = (int)stack[sp].getValue()+(int)stack[sp+1].getValue();
                            System.out.println("OPR: Suma "+(int)stack[sp].getValue()+" + "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Integer>(res);
                            break;
                        case 4:
                            sp--;
                            res = (int)stack[sp].getValue()-(int)stack[sp+1].getValue();
                            System.out.println("OPR: Resta "+(int)stack[sp].getValue()+" - "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Integer>(res);
                            break;
                        case 5:
                            sp--;
                            res = (int)stack[sp].getValue()*(int)stack[sp+1].getValue();
                            System.out.println("OPR: Multiplicar "+(int)stack[sp].getValue()+" * "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Integer>(res);
                            break;
                        case 6:
                            sp--;
                            res = (int)stack[sp].getValue()/(int)stack[sp+1].getValue();
                            System.out.println("OPR: Dividir "+(int)stack[sp].getValue()+" / "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Integer>(res);
                            break;
                        case 7:
                            sp--;
                            boolean res2 = (int)stack[sp].getValue()==(int)stack[sp+1].getValue();
                            System.out.println("OPR: Igual? "+(int)stack[sp].getValue()+" == "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Boolean>(res2);
                            break;
                        case 8:
                            sp--;
                            res2 = (int)stack[sp].getValue()!=(int)stack[sp+1].getValue();
                            System.out.println("OPR: No Igual? "+(int)stack[sp].getValue()+" != "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Boolean>(res2);
                            break;
                        case 9:
                            sp--;
                            res2 = (int)stack[sp].getValue()<(int)stack[sp+1].getValue();
                            System.out.println("OPR: Menor? "+(int)stack[sp].getValue()+" < "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Boolean>(res2);
                            break;
                        case 10:
                            sp--;
                            res2 = (int)stack[sp].getValue()<=(int)stack[sp+1].getValue();
                            System.out.println("OPR: Menor igual? "+(int)stack[sp].getValue()+" <= "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Boolean>(res2);
                            break;
                        case 11:
                            sp--;
                            res2 = (int)stack[sp].getValue()>(int)stack[sp+1].getValue();
                            System.out.println("OPR: Mayor? "+(int)stack[sp].getValue()+" > "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Boolean>(res2);
                            break;
                        case 12:
                            sp--;
                            res2 = (int)stack[sp].getValue()>=(int)stack[sp+1].getValue();
                            System.out.println("OPR: Mayor igual? "+(int)stack[sp].getValue()+" >= "+(int)stack[sp+1].getValue());
                            stack[sp] = new Data<Boolean>(res2);
                            break;
                    }
                    break;
                case CAR:
                    sp++;
                    stack[sp] = stack[base(i.getNi(),bp)+i.getDi()];
                    System.out.println("CAR: Cargando de la direccion "+(base(i.getNi(),bp)+i.getDi())+" el valor "+stack[sp].getValue().toString()+" a la direccion "+sp);
                    break;
                case ALM:
                    System.out.println("ALM: Almacenando "+stack[sp].getValue().toString()+" en la direccion "+(base(i.getNi(),bp)+i.getDi()));
                    stack[base(i.getNi(),bp)+i.getDi()] = stack[sp];
                    sp--;
                    break;
                case LLA:
                    stack[sp+1] = new Data<Integer>(base(i.getNi(), bp));
                    stack[sp+2] = new Data<Integer>(bp);
                    stack[sp+3] = new Data<Integer>(ip);
                    bp = sp+1;
                    ip = i.getDi();
                    break;
                case INS:
                    System.out.println("INS: Asignando "+i.getDi()+" espacios en el stack");
                    sp += i.getDi();
                    break;
                case SAL:
                    ip = i.getDi();
                    System.out.println("SAL: Salto incondicional a la linea "+i.getDi());
                    break;
                case SAC:
                    if (!(boolean)stack[sp].getValue()){
                        ip = i.getDi();
                        System.out.println("SAC: Salto condicional a la linea "+i.getDi());
                    }else{
                        System.out.println("SAC: No salto");
                    }
                    sp--;
                    break;
            }
        }
    }

    public static int base(int ni, int b){
        int b1;
        b1 = b;
        while (ni > 0){
            b1 = (int) stack[b1].getValue();
            ni--;
        }
        return b1;
    }

    public static void close(){
        ErrorLog.close();
        System.exit(0);
    }
}
