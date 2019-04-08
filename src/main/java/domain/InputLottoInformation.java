package domain;

import java.util.*;

public class InputLottoInformation {
    private Scanner sc ;
    private List<Lotto> lotto ;
    private List<Integer> lastweeklotto ;
    private final int lottoPrice = 1000;
    private final int maxLottoNo = 45;
    private final int minLottoNo = 1;
    private final int LottoBallCount = 6;
    private HashSet<Integer> overlapNo;
    private HashSet<Integer> overlapBonusNo;

    private void init() {
        sc = new Scanner(System.in);
        lotto = new ArrayList<>();
        lastweeklotto = new ArrayList<>();
        overlapNo = new HashSet<>();
        overlapBonusNo = new HashSet<>();
    }

    public int getLottoPurchasePrice() {
        init();
        int purchasePrice = 0;
        do {
            purchasePrice = checkPriceOnlyNumber(purchasePrice);
        } while (checkPrice(purchasePrice));
        return purchasePrice;
    }

    private int checkPriceOnlyNumber(int purchasePrice) {
        try {
            System.out.println(Message.lottoInputMessage.get("INPUT_PURCHASEMONEY"));
            purchasePrice = sc.nextInt();
        } catch (InputMismatchException ime) {
            sc= new Scanner(System.in);
            System.out.println(Message.errorMessage.get("ERROR_ONLYNUMBER"));
            return getLottoPurchasePrice();
        }
        return purchasePrice;
    }

    private boolean checkPrice(int pirce) {
        if (pirce <lottoPrice) { // 1000보다 적음 못삼
            System.out.println(Message.errorMessage.get("ERROR_MONEYSHORT"));
            return true;
        }
        return false;
    }

    public List<Lotto> getGeneratedLottoNumber(int money) {
        setLottoNumber(money);
        return lotto;
    }

    private void setLottoNumber(int money) {
        for (int i = money; i >=lottoPrice; i -= lottoPrice) {
            overlapNo = new HashSet<>();
            lotto.add(new Lotto(setSixNumber()));
        }
    }

    private List<Integer> setSixNumber() {
        List<Integer> lottonumber = new LinkedList<>();
        do {
            overlapNo.add((int) ((Math.random() * 44) + 1)); //1~45
        } while (overlapNo.size() < LottoBallCount);
        Iterator<Integer> it = overlapNo.iterator();
        while (it.hasNext()) {
            lottonumber.add((int) it.next());
        }
        return lottonumber;
    }

    public List<Integer> getLottoNumberOfLastWeek() {
        String number = "";
        do {
            lastweeklotto = new ArrayList<>();
            overlapBonusNo = new HashSet<>();
            System.out.println(Message.lottoInputMessage.get("INPUT_LASTWEEK_LOTTONUMBER"));
            number = sc.next();
            number = number.replaceAll("\\p{Z}","");
        } while (checkLastWeekLottoNumber(number));
        return lastweeklotto;
    }

    private boolean checkLastWeekLottoNumber(String number) { //,로구분
        boolean checkstate = false;
        String num[] = number.split(",");
        for (String no : num) {
            checkLottoBallNumber(no,checkstate); // 6개 번호 하나하나 보면서
        }
        if (!checkstate) {
            checkstate = checkLottoLength();
        }
        return checkstate;
    }

    private void checkLottoBallNumber(String no,boolean checkstate) {
        if (!checkstate) {
            if (checkLottoNumber(no)) {
                checkstate = true;
            }
            else {
                overlapBonusNo.add(Integer.parseInt(no));
                lastweeklotto.add(Integer.parseInt(no)); // 넘길 지난주 로또 번호 리스트에  저장
            }
        }
    }

    private boolean checkLottoNumber(String no) {
        if (checkOnlyNumber(no)) {
            return true;
        }
        int number = Integer.parseInt(no);
        if (checkLimitNumber(number) || checkOverlapNumber(number)) {
            return true;
        }
        return false;
    }

    private boolean checkLottoLength() {
        if (lastweeklotto.size() != LottoBallCount ) {
            System.out.println(Message.errorMessage.get("ERROR_SIXNUMBER"));
            return true;
        }
        return false;
    }

    private boolean checkOnlyNumber(String no) {
        int number = (no.charAt(0)-48);
        if (number < minLottoNo || number > maxLottoNo) {
            System.out.println(Message.errorMessage.get("ERROR_ONLYNUMBER"));
            return true;
        }
        return false;
    }

    private boolean checkLimitNumber(int number) {
        if ((number > maxLottoNo) || (number < minLottoNo)) {
            System.out.println(Message.errorMessage.get("ERROR_NUMBERLIMIT"));
            return true;
        }
        return false;
    }

    private boolean checkOverlapNumber(int number) {
        if (overlapBonusNo.contains(number) ) {
            System.out.println(Message.errorMessage.get("ERROR_OVERLAP"));
            return true;
        }
        overlapBonusNo.add(number);
        return false;
    }

    public int getBonusBallOfLastWeek() {
        int number = 0;
        do {
            number = setBonusBallOfLastWeek(number);
        } while (checkBonusBall(number)); // 보너스볼 체크
        return number;
    }

    private int setBonusBallOfLastWeek(int number) {
        try {
            System.out.println(Message.lottoInputMessage.get("INPUT_BONUSLOTTO"));
            number = sc.nextInt();
        } catch (InputMismatchException ime) {
            sc = new Scanner(System.in);
            System.out.println(Message.errorMessage.get("ERROR_ONLYNUMBER"));
            return getBonusBallOfLastWeek();
        }
        return number;
    }

    private boolean checkBonusBall(int number) {
        if (checkLimitNumber(number)) {
            return true;
        }
        if (checkOverlapNumber(number)) {
            return true;
        }
        return false;
    }
}
