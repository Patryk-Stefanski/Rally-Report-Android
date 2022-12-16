package wit.pstefans.rallyreport2.models.competitor

interface CompetitorStore {
    fun findAll(): List<CompetitorModel>
    fun create(competitor: CompetitorModel)
    fun update(competitor: CompetitorModel)
    fun delete(competitor: CompetitorModel)
}