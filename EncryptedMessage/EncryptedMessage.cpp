#include <iostream>
#include <string>
#include <vector>
using namespace std;

class Cypher{
private:
	string originalMessage_;
	string swappedMessage_;
	string alphabet_;
	string key_;
	string encryptedMessage_;
	int alphabetSize_;
	int keySize_;

	void parseMessage();
	string swapParts(string);
public:
	Cypher(string);
	~Cypher();
	string decryptMessage();
};
Cypher::Cypher(string message){
	originalMessage_ = message;
}
Cypher::~Cypher(){
}

string Cypher::swapParts(string str){
	string part1 = str.substr(0, str.length() / 2), part2 = str.substr(str.length() / 2);
	return part2 + part1;
}

void Cypher::parseMessage(){
	vector<string> parts;
	int lastPart = 0;
	int part = 0;
	for (int i = 0; i < swappedMessage_.length(); i++)
	{
		if (swappedMessage_[i] == '~' || i == swappedMessage_.length()-1){
			parts.push_back(swappedMessage_.substr(lastPart, i-part));
			part+=3;
			lastPart = i+1;
		}
	}
	alphabetSize_ = stoi(parts[0]);
	keySize_ = stoi(parts[2]);
	alphabet_ = parts[1].substr(0, alphabetSize_);
	encryptedMessage_ = parts[1].substr(alphabetSize_, parts[1].length() - keySize_);
	key_ = parts[1].substr(parts[1].length() - keySize_);
}

string Cypher::decryptMessage(){
	string realMessage = "";
	swappedMessage_ = swapParts(originalMessage_);
	parseMessage();
	return realMessage;
}

int main(){
	string messageOriginal = "fl k.ccfsIolskv.~312~ .Ifrckslovelvo";
	Cypher encrypted(messageOriginal);
	encrypted.decryptMessage();
	system("pause");
	return 0;
}