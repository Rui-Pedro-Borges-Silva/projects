import time

# Traditional matrix multiplication method (given code from c++)
def OnMult(m_ar, m_br):
    pha = []
    phb = []
    phc = []
    
    for i in range(0, m_ar):
        for j in range(0, m_ar):
            pha.append(1.0)

    for i in range(0, m_br):
        for j in range(0, m_br):
            phb.append(i + 1)

    Time1 = time.time()

    for i in range(0, m_ar):
        for j in range(0, m_br):
            temp = 0
            for k in range(0, m_ar):
                temp += pha[i * m_ar + k] * phb[k * m_br + j]
            phc.append(temp)

    Time2 = time.time()
    print("Time: " + str(Time2 - Time1) + " seconds")

    # Display 10 elements of the result matrix tto verify correctness
    print("Result matrix: ")
    for item in phc[:10]:
        print (item)
    return Time2 - Time1

# Line x line matrix multiplication
def OnMultLine(m_ar, m_br):
    # Initialize Matrices
    pha = []
    for i in range(0, m_ar):
        temp = []
        for j in range(0, m_ar):
            temp.append(1)
        pha.append(temp)

    phb = []
    for i in range(0, m_br):
        temp = []
        for j in range(0, m_br):
            temp.append(i+1)
        phb.append(temp)

    phc = []
    for i in range(0, m_ar):
        temp = []
        for j in range(0, m_ar):
            temp.append(0)
        phc.append(temp)

    Time1 = time.time()

    # Multiply Matrices
    for i in range(0, m_ar):
        for j in range(0, m_br):
            for k in range(0, m_ar):
                phc[i][k] += pha[i][j] * phb[j][k]

    Time2 = time.time()

    # Print time it took to make the calculations
    print("Time: " + str(Time2 - Time1) + " seconds")

    # Display 10 elements of the result matrix to verify correctness
    for item in phc[0][:10]:
        print (item)
    return Time2 - Time1

def main():
    while True:
        print("1. Multiplication")
        print("2. Line Multiplication")
        print("Selection?: ")
        option = int(input())

        if option == 0:
            break;
        
        print("Dimensions: lins=cols ? ")
        dimension = int(input())

        if option == 1:
            time = 0
            for _ in range(0, 6):
                time += OnMult(dimension, dimension)
            time /= 6
            print("Time", str(time), "seconds")
        elif option == 2:
            time = 0
            for _ in range(0, 6):
                time += OnMultLine(dimension, dimension)
            time /= 6
            print("Time", str(time), "seconds")

if __name__ == "__main__":
    main()
