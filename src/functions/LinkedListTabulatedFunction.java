package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    private static class FunctionNode {
        private FunctionPoint point;
        private FunctionNode prev = null;
        private FunctionNode next = null;

        public FunctionNode(){

            point = null;
        }

        public FunctionNode(FunctionNode prev, FunctionPoint point, FunctionNode next){
            this.point = new FunctionPoint(point);
            this.prev = prev;
            this.next = next;
        }

    }

    private FunctionNode head;
    private int sizeValue;

    private FunctionNode cacheNode;
    private int cacheIndex = 0;

    private void isFunctionPointIndexOutOfBoundsException(int index) {
        if (0 > index || index >= sizeValue) {
            throw new FunctionPointIndexOutOfBoundsException("Выход за границы точек");
        }
    }

    private FunctionNode getNodeByIndex(int index){
        isFunctionPointIndexOutOfBoundsException(index);
        if (Math.abs(index - cacheIndex) < index && Math.abs(index - cacheIndex) < sizeValue - index) {
            if (cacheIndex > index) {
                while (index != cacheIndex) {
                    cacheNode = cacheNode.prev;
                    --cacheIndex;
                }
            } else {
                while (index != cacheIndex ) {
                    cacheNode = cacheNode.next;
                    ++cacheIndex;
                }
            }
        } else if (index < sizeValue - index) {
            for (cacheIndex = 0, cacheNode = head.next; cacheIndex != index; ++cacheIndex) {
                cacheNode = cacheNode.next;
            }
        } else {
            for (cacheIndex = sizeValue - 1, cacheNode = head.prev; cacheIndex != index; --cacheIndex) {
                cacheNode = cacheNode.prev;
            }
        }
        return cacheNode;
    }

    private FunctionNode addNodeToTail(){
        ++sizeValue;
        FunctionNode newNode = new FunctionNode(head.prev, new FunctionPoint(), head);
        newNode.prev.next = newNode;
        head.prev = newNode;
        return newNode;
    }

    private FunctionNode addNodeToHead() {
        ++sizeValue;
        FunctionNode newNode = new FunctionNode(head, new FunctionPoint(), head.next);
        newNode.next.prev = newNode;
        head.next = newNode;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index){
        if (index == 0) return addNodeToHead();
        if (index == sizeValue) return addNodeToTail();
        ++sizeValue;
        FunctionNode oldNode = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode(oldNode.prev, new FunctionPoint(), oldNode);
        newNode.prev.next = newNode;
        oldNode.prev = newNode;
        cacheNode = newNode;
        cacheIndex = index;
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index){
        isFunctionPointIndexOutOfBoundsException(index);
        if (sizeValue < 3){
            throw new IllegalStateException();
        }
        FunctionNode deleteNode = getNodeByIndex(index);
        deleteNode.prev.next = deleteNode.next;
        deleteNode.next.prev = deleteNode.prev;
        --sizeValue;
        return deleteNode;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointCount){
        if(leftX >= rightX || pointCount < 2)
        {
            throw new IllegalArgumentException();
        }

        double step = (rightX - leftX) / (pointCount - 1);
        sizeValue = 1;
        head = new FunctionNode();
        FunctionNode zeroNode = new FunctionNode(head, new FunctionPoint(leftX,0), head);
        head.next = zeroNode;
        head.prev = zeroNode;
        for (int i = 1; i < pointCount; ++i){
            addNodeToTail().point = new FunctionPoint(leftX + step * i, 0);
        }
        cacheNode = head.next;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] value){
        if(leftX >= rightX || value.length < 2)
        {
            throw new IllegalArgumentException();
        }
        double step = (rightX - leftX) / (value.length - 1);
        sizeValue = 1;
        head = new FunctionNode();
        FunctionNode zeroNode = new FunctionNode(head, new FunctionPoint(leftX,value[0]), head);
        head.next = zeroNode;
        head.prev = zeroNode;
        for (int i = 1; i < value.length; ++i){
            addNodeToTail().point = new FunctionPoint(leftX + step *i, value[i]);
        }
        cacheNode = head.next;
    }

    public double getLeftDomainBorder(){
        return head.next.point.getX();
    }

    public double getRightDomainBorder(){
        return head.prev.point.getX();
    }

    public int getPointCount(){
        return sizeValue;
    }

    public FunctionPoint getPoint(int index){
        isFunctionPointIndexOutOfBoundsException(index);
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        isFunctionPointIndexOutOfBoundsException(index);
        FunctionNode node = getNodeByIndex(index);
        if(index != 0 && index != sizeValue-1) {
            if (node.prev.point.getX() < point.getX() && point.getX() < node.next.point.getX()) {
                node.point = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("х лежит вне интервала соседних точек");
            }
        } else if (index == 0){
            if(point.getX() < node.next.point.getX()){
                node.point = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("х лежит вне интервала соседних точек");
            }
        } else {
            if(node.prev.point.getX() < point.getX()){
                node.point = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("х лежит вне интервала соседних точек");
            }
        }
    }

    public double getPointX(int index){
        isFunctionPointIndexOutOfBoundsException(index);
        return getNodeByIndex(index).point.getX();
    }

    public double getPointY(int index){
        isFunctionPointIndexOutOfBoundsException(index);
        return getNodeByIndex(index).point.getY();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        isFunctionPointIndexOutOfBoundsException(index);
        setPoint(index, new FunctionPoint(x, getPointY(index)));
    }

    public void setPointY(int index, double y){
        isFunctionPointIndexOutOfBoundsException(index);
        getNodeByIndex(index).point.setY(y);
    }

    public double getFunctionValue(double x){
        if (x < head.next.point.getX() || x > head.prev.point.getX()) {
            return Double.NaN;
        }

        int index = 0;
        while (index < sizeValue && getNodeByIndex(index).point.getX() < x) {
            index++;
        }

        if (index < sizeValue) {
            double pointX = getNodeByIndex(index).point.getX();
            if (Math.abs(pointX - x) < 1e-10) {
                return getNodeByIndex(index).point.getY();
            }
        }

        double x1 = getNodeByIndex(index - 1).point.getX();
        double y1 = getNodeByIndex(index - 1).point.getY();
        double x2 = getNodeByIndex(index).point.getX();
        double y2 = getNodeByIndex(index).point.getY();

        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }

    public void deletePoint(int index){
        deleteNodeByIndex(index);

    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException{
        int index;
        for (index = 0; index < sizeValue && getPointX(index) < point.getX(); index++);

        if (index != sizeValue) {
            double existingX = getNodeByIndex(index).point.getX();
            if (Math.abs(existingX - point.getX()) < 1e-10) {
                throw new InappropriateFunctionPointException("есть точка, абсцисса которой совпадает с абсциссой добавляемой точки");
            }
        }
        else {
            addNodeByIndex(index).point = new FunctionPoint(point);
        }
    }

    public void printList(){
        FunctionNode printNode = head.next;
        for(int i = 0; i<sizeValue; ++i){
            System.out.println("№" + i + " x: " + printNode.point.getX() + " y: " + printNode.point.getY());
            printNode = printNode.next;
        }
    }

    public void printListR(){
        FunctionNode printNode = head.prev;
        for(int i = sizeValue-1; i>=0; --i){
            System.out.println("№" + i + " x: " + printNode.point.getX() + " y: " + printNode.point.getY());
            printNode = printNode.prev;
        }
    }
}
