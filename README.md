
# MilkManDemo PunkApi
Il progetto è stato scritto in kotlin con un pattern MVVM. E’ stato suddiviso in moduli : 

 **App**, **DI**, **Data**, **Model**, **Network**, **Local**, **UI** 

Per organizzare meglio le dipendenze di ogni modulo è stato usato un file *dependencies.gradle* che contiene tutte le dipendenze con le relative versioni. Ogni modulo utilizzerà le dipendenze che necessita.

Per i flussi è stato utilizzato **Kotlin Flow**

Il progetto prevede anche la **paginazione dei risultati** che arrivano dalla chiamata API ed è stato usato un PaginationScrollListener. Poteva essere usata anche la libreria di paging e il relativo PagingDataAdapter. (https://developer.android.com/topic/libraries/architecture/paging/v3-overview?hl=en)

Sono stati scritti **UnitTest** che testano tutte le logiche del repository usando la libreria **MockK**

Nel progetto è stata usata una **cache su DB** in grado di fornire i risultati anche offline

Sono state inserite logiche per la **gestione degli errori**
