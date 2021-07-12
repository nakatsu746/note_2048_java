package game;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GrowNumber{
  private int[][] fieldNumber = new int[4][4];
  private String[] commandKey = {"w", "s", "a", "d"};
  private int countMove;

// *********************** Game Initialize Setting *****************************
// コンストラクタ
  public GrowNumber(){
    countMove = 0;
    InitialSetting();
  }

// 初期設定
  public void InitialSetting(){
    // ランダムに3箇所、数字2を配置
    for(int i=0; i<3; i++)
      RandomPlace();
    GameCondition();
  }

// *********************** Game Flow *******************************************
// ゲーム全体の流れ
  public void GrowNumberGame(){
    gameOver:
    while(true){
      String command;
      boolean numberJudge;
      do {
        // コマンド入力
        command = CommandInput();
        // 数字を移動
        boolean numberMove = NumberMove(command);
        // 数字の判定
        numberJudge = NumberJudge(command, numberMove);
        // ゲームの終了判定
        if(GameOver())
          break gameOver;
        // 数字が全く動かなかった場合、もう一度コマンド入力を行う
        if(!numberJudge)
          System.out.println("コマンド入力が不正です（一度も数字が移動していません）");
      } while(!numberJudge);
      // 操作回数をカウント
      countMove++;
      // ランダム配置
      RandomPlace();
      // ゲーム状況の表示
      GameCondition();
    }
  }

// *********************** Game Condition **************************************
// 状態表示
  public void GameCondition(){
    //

    System.out.println();
    System.out.println("-----------------------------");
    for(int i=0; i<fieldNumber.length; i++){
      for(int j=0; j<fieldNumber[i].length; j++){
        System.out.print("|");
        // 0の場合は空白で表示
        if(fieldNumber[i][j] == 0){
          System.out.print("      ");
        } else{
          // 桁数ごとに表示を中央揃えにする
          String num = String.valueOf(fieldNumber[i][j]);
          if(num.length() == 1){
            System.out.print("   " + num + "  ");
          } else if(num.length() == 2){
            System.out.print("  " + num + "  ");
          } else if(num.length() == 3){
            System.out.print("  " + num + " ");
          } else if(num.length() == 4){
            System.out.print(" " + num + " ");
          } else {
            System.out.print(String.format("%6d", num));
          }
        }
      }
      System.out.println("|");
      System.out.println("-----------------------------");
    }
  }

// *********************** Random Placement ************************************
// ランダム配置
  public void RandomPlace(){
    // 空白スペースの取得
    ArrayList<Integer> blankSpace = new ArrayList<Integer>();
    blankSpace = BlankSpace();
    // 配置する場所をランダムに取得
    Random random = new Random();
    int randomIndex = random.nextInt(blankSpace.size());
    int randomNum = blankSpace.get(randomIndex);
    // 数字の2を配置
    int line = randomNum / fieldNumber.length;
    int column = randomNum % fieldNumber[0].length;
    fieldNumber[line][column] = 2;
  }

// 空白スペースの取得
  public ArrayList<Integer> BlankSpace(){
    ArrayList<Integer> blankSpace = new ArrayList<Integer>();
    // 空白スペースを取得
    for(int i=0; i<fieldNumber.length; i++){
      for(int j=0; j<fieldNumber[i].length; j++){
        if(fieldNumber[i][j] == 0)
          blankSpace.add(i*fieldNumber.length + j);
      }
    }
    return blankSpace;
  }

// *********************** Command *********************************************
// コマンド入力
  public String CommandInput(){
    String command = "";
    // コマンドの適性確認
    boolean commandOK = false;
    while(!commandOK){
      // コマンド入力
      System.out.println("コマンドを入力してください");
      System.out.println("（ W：↑ ／ S：↓ ／ A：← ／ D：→ ）");
      Scanner sc = new Scanner(System.in);
      command = sc.nextLine();
      // 入力コマンドの確認
      commandOK = CommandCheck(command);
    }
    return command.toLowerCase();
  }

// コマンド入力の確認
  public boolean CommandCheck(String command){
    if(Arrays.asList(commandKey).contains(command.toLowerCase()))
      return true;
    System.out.println("コマンド入力が不正です。");
    return false;
  }

// *********************** Number Movement *************************************
// 数字の移動処理
  public boolean NumberMove(String command){
    boolean swaped = false;
    switch(command){
    case "w":
      swaped = MoveVertical("up");
      break;
    case "s":
      swaped = MoveVertical("down");
      break;
    case "a":
      swaped = MoveHorizontal("left");
      break;
    case "d":
      swaped = MoveHorizontal("right");
      break;
    }
    return swaped;
  }

// 上下の垂直移動の処理
  public boolean MoveVertical(String direction){
    boolean swaped = false;
    int countSwap;
    // 垂直移動の処理
    while(true){
      countSwap = 0;

      for(int i=0; i<fieldNumber[0].length; i++){
        for(int j=0; j<fieldNumber.length-1; j++){
          // 詰める方向に分けて処理を行う
          switch(direction){
          case "up":
            // [上詰め]上側が0 & 下側が0以外 → swap
            if(fieldNumber[j][i]==0 && fieldNumber[j+1][i]!=0){
              swapVertical(i, j);
              // swap処理を行ったらtrueにする
              swaped = true;
              countSwap++;
            }
            break;
          case "down":
            // [下詰め]上側が0以外 & 下側が0 → swap
            if(fieldNumber[j][i]!=0 && fieldNumber[j+1][i]==0){
              swapVertical(i, j);
              // swap処理を行ったらtrueにする
              swaped = true;
              countSwap++;
            }
            break;
          }
        }
      }
      // 一度もswapが行われなくなったらループを終了
      if(countSwap == 0)
        break;
    }
    // 一度もswapが行われていなかった場合、もう一度コマンド入力を実施
    return swaped;
  }

// 左右の水平移動の処理
  public boolean MoveHorizontal(String direction){
    boolean swaped = false;
    int countSwap;
    // 水平移動の処理
    while(true){
      countSwap = 0;

      for(int i=0; i<fieldNumber.length; i++){
        for(int j=0; j<fieldNumber[i].length-1; j++){
          // 詰める方向に分けて処理を行う
          switch(direction){
          case "left":
            // [左詰め]左側が0 & 右側が0以外 → swap
            if(fieldNumber[i][j]==0 && fieldNumber[i][j+1]!=0){
              swapHorizontal(i, j);
              // swapを行ったらtrueにする
              swaped = true;
              countSwap++;
            }
            break;
          case "right":
            // [右詰め]左側が0以外 & 右側が0 → swap
            if(fieldNumber[i][j]!=0 && fieldNumber[i][j+1]==0){
              swapHorizontal(i, j);
              // swapを行ったtrueにする
              swaped = true;
              countSwap++;
            }
            break;
          }
        }
      }
      // 一度もswapが行われなくなったらループを終了
      if(countSwap == 0)
        break;
    }
    // 一度もswapが行われていなかった場合、もう一度コマンド入力を実施
    return swaped;
  }

// 水平方向の交換処理
  public void swapHorizontal(int i, int j){
    int temp = fieldNumber[i][j];
    fieldNumber[i][j] = fieldNumber[i][j+1];
    fieldNumber[i][j+1] = temp;
  }

// 垂直方向の交換処理
  public void swapVertical(int i, int j){
    int temp = fieldNumber[j][i];
    fieldNumber[j][i] = fieldNumber[j+1][i];
    fieldNumber[j+1][i] = temp;
  }

// *********************** Number Judgment *************************************
// 数字の合わせ判定
  public boolean NumberJudge(String command, boolean numberMove){
    // 一度でも数字の足算が行われたかを取得
    boolean numberPlus = false;
    // 垂直／水平方向に分けて処理を行う
    switch(command){
    // 上方向の処理
    case "w":
      numberPlus = NumberJudgeUp();
      break;
    // 下方向の処理
    case "s":
      numberPlus = NumberJudgeDown();
      break;
    // 左方向の処理
    case "a":
      numberPlus = NumberJudgeLeft();
      break;
    // 右方向の処理
    case "d":
      numberPlus = NumberJudgeRight();
      break;
    }
    // 数字の足算を行った場合、もう一度数字を移動させる
    if(numberPlus)
      NumberMove(command);
    // コマンド入力をしてから数字が全く移動しなかった場合、もう一度コマンド入力からやり直し
    if(!numberPlus && !numberMove)
      return false;
    else
      return true;
  }

// 上方向の数字の合わせ判定
  public boolean NumberJudgeUp(){
    // 一度でも数字の足算が行われたかを取得
    boolean numberPlus = false;

    for(int i=0; i<fieldNumber[0].length; i++){
      for(int j=0; j<fieldNumber.length-1; j++){
        // 上側の数 == 左側の数
        if((fieldNumber[j][i] == fieldNumber[j+1][i]) && (fieldNumber[j][i] != 0)){
          fieldNumber[j][i] *= 2;
          fieldNumber[j+1][i] = 0;
          j++;
          numberPlus = true;
          continue;
        }
      }
    }
    return numberPlus;
  }

// 上方向の数字の合わせ判定
  public boolean NumberJudgeDown(){
    // 一度でも数字の足算が行われたかを取得
    boolean numberPlus = false;

    for(int i=0; i<fieldNumber[0].length; i++){
      for(int j=fieldNumber.length-1; j>0; j--){
        // 下側の数 == 上側の数
        if((fieldNumber[j][i] == fieldNumber[j-1][i]) && (fieldNumber[j][i] != 0)){
          fieldNumber[j][i] *= 2;
          fieldNumber[j-1][i] = 0;
          j++;
          numberPlus = true;
          continue;
        }
      }
    }
    return numberPlus;
  }

// 左方向の数字の合わせ判定
  public boolean NumberJudgeLeft(){
    // 一度でも数字の足算が行われたかを取得
    boolean numberPlus = false;

    for(int i=0; i<fieldNumber.length; i++){
      for(int j=0; j<fieldNumber[i].length-1; j++){
        // 左側の数 == 右側の数
        if((fieldNumber[i][j] == fieldNumber[i][j+1]) && (fieldNumber[i][j] != 0)){
          fieldNumber[i][j] *= 2;
          fieldNumber[i][j+1] = 0;
          j++;
          numberPlus = true;
          continue;
        }
      }
    }
    return numberPlus;
  }

// 右方向の数字の合わせ判定
  public boolean NumberJudgeRight(){
    // 一度でも数字の足算が行われたかを取得
    boolean numberPlus = false;

    for(int i=0; i<fieldNumber.length; i++){
      for(int j=fieldNumber[i].length-1; j>0; j--){
        // 右側の数 == 左側の数
        if((fieldNumber[i][j] == fieldNumber[i][j-1]) && (fieldNumber[i][j] != 0)){
          fieldNumber[i][j] *= 2;
          fieldNumber[i][j-1] = 0;
          j++;
          numberPlus = true;
          continue;
        }
      }
    }
    return numberPlus;
  }

// *********************** Game Judgment ***************************************
// ゲーム終了判定
  public boolean GameOver(){
    // 空きスペースを取得
    ArrayList<Integer> blankSpace = new ArrayList<Integer>();
    blankSpace = BlankSpace();
    // 空きスペースが0の場合、ゲーム終了
    if(blankSpace.size() == 0){
      System.out.println("***** GAME OVER *****");
      System.out.println("　スコア　 ： " + MaxNumber());
      System.out.println("　操作回数 ： " + countMove);
      return true;
    }
    return false;
  }

// スコア（最大値）の取得
  public int MaxNumber(){
    int maxNum = 0;
    for(int i=0; i<fieldNumber.length; i++){
      for(int j=0; j<fieldNumber[i].length; j++){
        if(maxNum < fieldNumber[i][j])
          maxNum = fieldNumber[i][j];
      }
    }
    return maxNum;
  }
}
