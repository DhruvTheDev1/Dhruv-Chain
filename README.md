### Dhruv-Chain
This is my personal learning project that helps me to better understand blockchain technology by developing one. I built a basic blockchain in Java by following a tutorial and diving deep into how blockchains work. Instead of blindly copy-pasting, I took
time to understand each concept from hashes and chaining to mining and how nonce is used in blockchain. This is a proof of implementation to help understand blockchain technology.

# Part 1 

## What I built
- My first very own blockchain (Dhruv-chain)
- made up of blocks that stores data
- Has digital signatures that chains the block together
- Requires proof of work mining to validate new blocks
- Chain validation to detect tampering

### Blocks Store data
Each block contains:
- data - some meessage
- previousHash - hash of the previous block (first block is Genesis block hence previous hash is 0)
- Hash - current blocks hash
- nonce - number that we increment when mining until difficulty is met
- Timestamp - the time the block was created

## Hashes
A block contains the SHA256 hash of the previous block, list of transactions, the current time, address, an arbitrary number called a 'nonce', etc.
Each block stores the previous blocks hash, creating a chain. If someone tried to tamper with a block, its entire hash changes and will break the chain.
Hashing is used to ensure data integrity and security

E.g.
A block will contain a digital signature such as:
- 2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824
modifying even a single character will completely change it such as:
- 5b175fdd8cdfd78c967643ad01f951210364c65642dbf5410f3fb778160585db

## Validating the blockchain
This checks:
- If the current blocks hash matches the calculated hash
- If block's previousHash matches the actual hash of the previous block
- If the block was properly mined (hash starts with enough zeros) hence meets the difficulty rate

## Proof of work
For example bitcoin curr has a difficulty rate of:
126.27 T
(126,271,255,279,307.00)
A miner keeps increasing the nonce and your current timestamp until they are able to generate a hash that has a certain number of leading zeros.
E.g. difficulty rate = 5 "00000"
The current reward for mining: 3.125 BTC

## Credits
Based on the tutorial ["Creating Your First Blockchain with Java"](https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa) — 
but rewritten, explored, and deeply understood line-by-line with my own explanations and comments.
