package domain.entities;

data class Good(
    val sales: Int,
    val stock: Int,
    val vendor_code: String,
    val rank: Int
) {
    override fun equals(other: Any?): Boolean {

        if (other is Good) {
            return this.vendor_code == (other).vendor_code;
        }
        return false;

    }

    override fun toString(): String {
        return String.format("%d   %s : sales %d  stocks %d //", rank, vendor_code, sales, stock);
    }

    override fun hashCode(): Int {
        return vendor_code.hashCode()
    }


}




