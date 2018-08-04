


PW5HssK8HxWgz8X5TqHefsgPRZvVEDo5snP91t53fhVCevMUFEWaj   default


PW5JgDFo269QeChxP1mp79wePsRvL2aq36CFbgMMaJrqgNqW2McK2   greenKeepWallets



Private key: 5HzUF3EQpLyy87r8w7i4EbmUJDRwBguEUMrGjBGgkiQ5Z9dDJYd
Public key: EOS8NkFJPdrZrpYrcZPLf5v4iYcLB6vKBkZnrmRxpqkYs7Xj8QCFk

Private key: 5KiRUuurFN3YsMccS56DzpnRHy4epBXDnQ2ugxi2r7dnfZJcF29
Public key: EOS6ZSbB5LXLaVkG19bvtsWEWmMwcxpxS6cmPx21SqVA1d5twEcdi

Private key: 5KQ2X2o5N7Ke54vwzsvnbeCBhoieCutsfyog8VNeHBGxkqgpd2V
Public key: EOS7QQcFUCpor5c831yoAG4hqA5uicpnigCDm7mfgLq6KtuXaEYRQ

Private key: 5HtJxXJypg74bxgyNWbZ3AJbqqrcZrQJmTEXEAmStvnUuk7vWWc
Public key: EOS7J62FrT5ZJgTaxYZ78XPvqCxJLtw2gbn58CBpapYVaktfms6Xc

cleos wallet import --name greenKeepWallets --private-key 

cleos create account eosio greenkeep 

cleos push action greenkeep regSensor '["sensor1", 111, 50, 29]' -p greenkeep@active

eosiocpp -o /opt/eosio/bin/contracts/greenkeep/greenkeep.wasm /opt/eosio/bin/contracts/greenkeep/greenkeep.cpp
eosiocpp -g /opt/eosio/bin/contracts/greenkeep/greenkeep.abi /opt/eosio/bin/contracts/greenkeep/greenkeep.cpp
cleos set contract greenkeep /opt/eosio/bin/contracts/greenkeep -p factory@active