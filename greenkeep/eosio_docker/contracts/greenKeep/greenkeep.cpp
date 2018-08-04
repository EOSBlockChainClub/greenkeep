#include <eosiolib/eosio.hpp>
#include <eosiolib/print.hpp>
using namespace eosio;

class greenkeep : public eosio::contract {
  public:
      greenkeep(account_name s):
        contract(s), // initialization of the base class for the contract
        sensors(s, s) // initialize the table with code and scope NB! Look up definition of code and scope
      {
      }

      /// @abi action
      /*void create(account_name username, uint64_t ssn, const std::string& fullname, uint64_t age) {
        require_auth(username);
        // Let's make sure the primary key doesn't exist
        // _people.end() is in a way similar to null and it means that the value isn't found
        eosio_assert(_people.find(ssn) == _people.end(), "This SSN already exists in the addressbook");
        _people.emplace(get_self(), [&]( auto& p ) {
           p.ssn = ssn;
           p.fullname = fullname;
           p.age = age;
        });*/

        /// @abi action
        void regsensor(account_name sensor_account, uint64_t id){
          //require_auth(_self);
          
          //eosio_assert(is_account(sensor_account), "sensor account does not exist");
          sensor_table sensors(_self, _self);
          sensors.emplace(_self, [&](auto& sensor){
            sensor.sensor_account = sensor_account;
            //sensor.id = id;
          });
        }

        /// @abi action
        void adddata(const account_name sensor, const uint64_t distance_traveled_per_day, const uint64_t weight, const uint64_t age, const uint64_t batchdate, const std::string location){
          require_auth(sensor);
          sensor_table sensors(_self, _self);
          auto index = sensors.find(sensor);
          //eosio_assert(index != sensors.end(), "sensor is not registered");
          sensorData_table data_table(_self, _self);
          data_table.emplace(_self, [&](auto& sensorData){
            sensorData.sensor_account = sensor;
            sensorData.distance_traveled_per_day = distance_traveled_per_day;
            sensorData.weight = weight;
            sensorData.age = age;
            sensorData.batchdate = batchdate;
            sensorData.location = location;
            sensorData.id = data_table.available_primary_key();
          });
        }


       

  private: 
    // Setup the struct that represents the row in the table
    /// @abi table people
    /*struct person {
      uint64_t ssn; // primary key, social security number
      std::string fullname;
      uint64_t age;

      uint64_t primary_key()const { return ssn; }
      uint64_t by_age()const { return age; }
    };*/
    
    /// @abi table sensor
    struct sensor{
      uint64_t id;
      account_name sensor_account;

      uint64_t primary_key()const {return id;}
    };

    /// @abi table
    typedef eosio::multi_index<N(sensors), sensor> sensor_table;
    sensor_table sensors;

    /// @abi table sensordata
    struct sensorData{
      uint64_t id;
      account_name sensor_account;
      uint64_t distance_traveled_per_day;
      uint64_t weight;
      uint64_t age;
      uint64_t batchdate;
      std::string location;


      uint64_t primary_key()const { return id; }
    };

    // We setup the table:
    //typedef eosio::multi_index< N(people), person/*, indexed_by<N(byage), const_mem_fun<person, uint64_t, &person::by_age>>*/>  people;
    //people _people;

    /// @abi table
    typedef eosio::multi_index<N(sensordata), sensorData> sensorData_table;
};

EOSIO_ABI( greenkeep, (regsensor)(adddata) )
