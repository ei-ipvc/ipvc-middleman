package pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto

data class ScheduleDTO (
  val className: String,
  val classType: String,
  val start: String,
  val end: String,
  val stamp: String,
  val id: String,
  val teachers: List<String>,
  val room: String,
  val statusColor: String,
)

/*

title: 'IEOP I [PL1] ESTG - S.3.5',
start: '2024-09-16T09:00:00',
end: '2024-09-16T11:00:00',
datauc: '3003025-Integração da Empresa - Opção I-IEOP I-PL-PL1-Aula 9119.3.3003025-PL-1',
dataeventoid: '118810',
datadatainicio: '2024-09-16 09:00:00',
datadatafim: '2024-09-16 11:00:00',
datadocentes: '<div style="font-size: 11px; padding-top: 8px; color: #2a6284;">&bull; Luís Óscar Araújo Barreiros</div>',
datasala: '<div style="font-size: 11px; padding-top: 4px; color: #2a6284;">&bull; ESTG - S.3.5</div>',
color: '#cccccc',
textColor: '#868686'

*/