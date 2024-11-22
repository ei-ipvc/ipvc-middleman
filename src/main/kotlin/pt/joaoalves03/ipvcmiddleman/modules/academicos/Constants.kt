package pt.joaoalves03.ipvcmiddleman.modules.academicos

object Constants {
  const val LOGIN_ENDPOINT = "https://academicos.ipvc.pt/netpa/ajax?stage=loginstage"
  const val TUITION_FEES_ENDPOINT = "https://academicos.ipvc.pt/netpa/DIFTasks?_PR_=1&_AP_=9&_MD_=1&_SR_=173&_ST_=1"
  const val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:122.0) Gecko/20100101 Firefox/122.0"
  const val PROFILE_ENDPOINT = "https://academicos.ipvc.pt/netpa/page?stage=situacaodealuno"
  const val BASE_ENDPOINT = "https://academicos.ipvc.pt/netpa/"
  const val GRADES_ENDPOINT = "https://academicos.ipvc.pt/netpa/ajax/consultanotasaluno/inscricoes?_dc=1726447964851&cdLectivoFilter=null&periodoFilter=null&anoCurricular=null&estadoFilter=null&disciplinaFilter=null&group=%5B%7B%22property%22%3A%22CD_LECTIVO%22%2C%22direction%22%3A%22desc%22%7D%5D&sort=%5B%7B%22property%22%3A%22CD_LECTIVO%22%2C%22direction%22%3A%22DESC%22%7D%2C%7B%22property%22%3A%22CD_DURACAO%22%2C%22direction%22%3A%22ASC%22%7D%2C%7B%22property%22%3A%22DS_DISCIP%22%2C%22direction%22%3A%22ASC%22%7D%5D"
  const val CURRICULAR_UNIT_STATUS_ENDPOINT = "https://academicos.ipvc.pt/netpa/ajax/situacaodealuno/tabelaPlanoEstudos?page=1&start=0&limit=-1&group=[{\"property\":\"CD_A_S_CUR\",\"direction\":\"ASC\"}]&sort=[{\"property\":\"CD_A_S_CUR\",\"direction\":\"ASC\"}]"
  const val DUMMY_ACTIVATION_ENDPOINT = "https://academicos.ipvc.pt/netpa/page?stage=ConsultaNotasAluno"
}