package Beadando1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Game {
    static int boardLength = 0;
    static Player[] players = null;
    static ArrayList<Integer> diceRole = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Game started.");
        ArrayList<Player> playersOut = new ArrayList<>();
        int round = 0;
        String lasPlayerOut = "";
        BoardCard[] boardArray = GameSetup();

        while(playersOut.size() < 2){
            for(Player player : players){
                if(playersOut.contains(player) || player.getMoney() <= 0){
                    if(!playersOut.contains(player)){
                        playersOut.add(player);
                        lasPlayerOut = player.getName();
                    }
                    continue;
                }

                int steps = diceRole.get(round % diceRole.size());
                player.move(steps, boardArray.length);
                int playerPosition = player.getPosition();
                BoardCard currentCard = boardArray[playerPosition];

                if(currentCard instanceof Property){
                    Property property = (Property) currentCard;
                    if(!property.hasOwner()){
                        if(player.shouldBuy() && player.canPay(property.getValue())){
                            player.pay(property.getValue());
                            property.setOwnerName(player.getName());
                        }
                    } else if(!property.getOwnerName().equals(player.getName())){
                        Player owner = null;
                        for(Player p : players){
                            if(p.getName().equals(property.getOwnerName())){
                                owner = p;
                                break;
                            }
                        }

                        if(owner != null){
                            int rent = property.getValue();
                            if(player.canPay(rent)){
                                player.pay(rent);
                                owner.addMoney(rent);
                            } else {
                                playersOut.add(player);
                                lasPlayerOut = player.getName();
                            }
                        }
                    }
                } else if(currentCard instanceof Service){
                    int fee = currentCard.getValue();
                    if(player.canPay(fee)){
                        player.pay(fee);
                    } else {
                        playersOut.add(player);
                        lasPlayerOut = player.getName();
                    }
                } else if(currentCard instanceof Lucky){
                    player.addMoney(currentCard.getValue());
                }

                if(player.getMoney() <= 0 && !playersOut.contains(player)){
                    playersOut.add(player);
                    lasPlayerOut = player.getName();
                }
            }
            round++;
        }

        if(playersOut.size() >= 2){
            System.out.println("Second player out: " + lasPlayerOut);
        }

    }

    public static BoardCard[] GameSetup() {
        BoardCard[] boardCardArray = null;
        try (BufferedReader br = new BufferedReader(new FileReader("Beadando1/gameData.txt"))) {
            boardLength = Integer.parseInt(br.readLine());
            boardCardArray = new BoardCard[boardLength];

            String line;
            while ((line = br.readLine()) != null && !line.equals("--")) {
                String[] parts = line.split(";");
                String type = parts[0];
                int value = Integer.parseInt(parts[1]);
                int position = Integer.parseInt(parts[2])-1;

                if (type.equals("property")) {
                    boardCardArray[position] = new Property(type, position);
                } else if (type.equals("service")) {
                    boardCardArray[position] = new Service(type, value, position);
                } else if (type.equals("lucky")) {
                    boardCardArray[position] = new Lucky(type, value, position);
                }
            }

            PlayerSetup(br, line);
            DiceRole(br, line);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return boardCardArray;
    }

    public static void PlayerSetup(BufferedReader br, String line) throws IOException {
        int playersCount;
            
            playersCount = Integer.parseInt(br.readLine());
            players = new Player[playersCount];
            while ((line = br.readLine()) != null && !line.equals("--")) {
                playersCount--;
                String[] parts = line.split(";");
                String name = parts[0];
                String type = parts[1];
                if (type.equals("careful")) {
                    players[playersCount] = new Careful(name);
                } else if (type.equals("greedy")) {
                    players[playersCount] = new Greedy(name);
                } else if (type.equals("tactical")) {
                    players[playersCount] = new Tactical(name);
                }
            }
    }

    public static void DiceRole(BufferedReader br, String line) throws IOException {
        while((line = br.readLine()) != null && !line.equals("--")) {
            diceRole.add(Integer.parseInt(line));
        }
    }
}
