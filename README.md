### Dhruv-Chain
This is my personal learning project to deeply understand blockchain technology by developing one. I built my very own blockchain in Java by following a tutorial and diving deep into how blockchains work. I carefully studied every concept - from hashing
and chaining blocks, to mining and digital signatures, and finally implementing secure transactions and wallets. This is a proof of implementation to help me understand blockchain technology.

# Part 1 - Building the blockchain core
<img width="828" height="323" alt="image" src="https://github.com/user-attachments/assets/e65ad4ca-0109-4006-b52f-b242b3a59880" />

## What I built
- A simple blockchain made up of linked blocks storing data
- Each block contains:
  - data (a message)
  - Previous block's hash
  - Current blocks hash
  - Nonce (a number used for mining)
  - Timestamp (the time the block was created)

- Implemented proof of work (mining) to validate new blocks by finding a hash that meets the required number of leading zeros (difficulty).
- Validated the blockchain which checks:
  - If the current blocks hash matches the calculated hash
  - Verifies the current block correctly references the previous blocks hash
  - Confirms the block meets the difficulty requirement 

# Part 2 - Adding transactions, wallets and digital signatures
Part 2 expands the blockchain to support real cryptocurrency features by implementing wallets, sending signed transactions and digital signatures
This transforms the chain from storing simple messages to managing actual value transfers — effectively creating a simple crypto coin


## Wallet
- Generates a public and private key using  using elliptic-curve cryptography (generates mathematically linked public and private keys)
- The public key acts as the wallets address and the private key is used to sign the transaction ensuring only the owner can sign the transaction
## Transactions
- Transactions replaces the basic data stored and each transaction records
  - Senders public key
  - Receiver's public key
  - Amount transferred
  - Transaction inputs and outputs (tracks ownership and unspent funds)
  - Digital signature to verify the transaction and authenticity
## Digital Signatures
- The private key is paired with the public key in asymmetric cryptography and is used to create a digital signature which is then sent to the recipient.
- Miners can then use the signer's public key to verify the signature.
## Inputs and Outputs (UTXO)
- A UTXO (Unspent Transaction Output) is a fundamental concept in cryptocurrencies such as Bitcoin. It specifies the amount leftover after a transaction (the change) that can be used in future transactions.
- Transactions undergo validation checks such as enough coins for transactions, etc. Upon success, it updates the UTXO by marking inputs spent and outputs as unspent and updates the blockchain state.
## Transaction Processing
Transactions undergo validation checks such as valid digital signatures, ensuring the blocks hash is correct, input transaction matches output transaction, etc.

# Outcome
With this changes, the basic blockchain has now developed into a functional cryptocurrency system that allows users to create new wallets which provides public and private keys. Secures the transfer of funds using digital signature algorithm
to prove ownership and allows users to make transactions on the blockchain.

<img width="798" height="361" alt="image" src="https://github.com/user-attachments/assets/84496441-5a90-4684-8b0f-a8db392f3620" />

## Credits
Based on the tutorial ["Creating Your First Blockchain with Java"](https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa) — 
but rewritten, explored, and deeply understood line-by-line with my own explanations and comments.
