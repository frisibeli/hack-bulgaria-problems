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
	int messageSize_;

	void parseMessage();
	string swapParts(string);
	int* getAlphabetIndexes(string);
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

int* Cypher::getAlphabetIndexes(string word){
	int* indexes = new int[word.length()];
	for (int i = 0; i < word.length(); i++)
	{
		indexes[i] = alphabet_.find(word[i]);
	}
	return indexes;
}

void Cypher::parseMessage(){
	vector<string> parts;
	int lastPart = 0;
	int part = 0;
	for (int i = 0; i < swappedMessage_.length(); i++)
	{
		if (swappedMessage_[i] == '~' || i == swappedMessage_.length() - 1){
			parts.push_back(swappedMessage_.substr(lastPart, i - part));
			part += 3;
			lastPart = i + 1;
		}
	}
	alphabetSize_ = stoi(parts[0]);
	keySize_ = stoi(parts[2]);
	alphabet_ = parts[1].substr(0, alphabetSize_);
	messageSize_ = parts[1].length() - keySize_;
	encryptedMessage_ = parts[1].substr(alphabetSize_, messageSize_);
	key_ = parts[1].substr(parts[1].length() - keySize_);
}

string Cypher::decryptMessage(){
	swappedMessage_ = swapParts(originalMessage_);
	parseMessage();

	string realMessage = "", keyString = "";
	int *inputArray, *keyArray, *outputArray = new int[encryptedMessage_.length()];

	for (int i = 0; i < (encryptedMessage_.length() / keySize_ + keySize_); i++)
	{
		keyString += key_;
	}
	inputArray = getAlphabetIndexes(encryptedMessage_);
	keyArray = getAlphabetIndexes(keyString);
	for (int i = 0; i < encryptedMessage_.length(); i++)
	{
		outputArray[i] = (inputArray[i] - keyArray[i] + alphabetSize_) % alphabetSize_;
		realMessage += alphabet_[outputArray[i]];
	}
	return realMessage;
}

int main(){
	string messageOriginal = "o?uin uw?stutnfwat?~413~orwa? thfuisnnrsiu";
	Cypher encrypted(messageOriginal);
	cout<<encrypted.decryptMessage();
	system("pause");
	return 0;
}