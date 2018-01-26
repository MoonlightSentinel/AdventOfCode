#include <iostream>
#include <fstream>
#include <string>

using std::string;
using std::cout;
using std::cerr;
using std::endl;

string solve(const string& input, const size_t length) {

    string result = input;
    result.reserve(length);

    while(result.length() < length) {
        auto cur = result.cend(),
             end = result.cbegin();

        result.push_back('0');

        while(cur-- != end) {
            if(result.length() == length) break;

            result.push_back(*cur == '0' ? '1' : '0');
        }
    }

    auto end = result.cend();

    do {

        auto cur = result.begin();
        auto out = result.begin();

        char a,b;

        while (cur < end) {
            a = *cur;   cur++;
            b = *cur;   cur++;

            *out = a == b ? '1' : '0';
            out++;
        }

        end = out;
    } while((end - result.cbegin()) % 2 == 0);

    result.resize(end - result.cbegin());
    return result;
}

int main() {
    cout << "Test: " << solve("10000", 20) << endl;
    cout << "TaskA: " << solve("11101000110010100", 272) << endl;
    cout << "TaskB: " << solve("11101000110010100", 35651584) << endl;
}